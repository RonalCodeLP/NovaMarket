# Keycloak — NovaMarket

> Guía completa de identidad, autenticación y autorización para este proyecto.

---

## ¿Qué es Keycloak y para qué sirve aquí?

Keycloak es un servidor de **identidad (IdP — Identity Provider)**. Su trabajo es responder dos preguntas:

- **¿Quién eres?** → Autenticación (login con usuario y contraseña)
- **¿Qué puedes hacer?** → Autorización (qué pantallas y acciones tienes permitidas)

En NovaMarket, Keycloak centraliza todo eso. Tus microservicios y tu app Angular **no guardan contraseñas ni manejan sesiones** — eso es 100% responsabilidad de Keycloak.

---

## Los 5 conceptos que necesitas entender

### 1. Realm — el "mundo" de tu app

Un realm es un espacio aislado con sus propios usuarios, roles y configuraciones.
El realm de este proyecto se llama **`novamarket`**.

Todo lo que configures en `novamarket` no afecta a otros realms (por ejemplo, si tuvieras un realm `dev` y uno `prod` separados).

### 2. Client — cómo se registra tu app en Keycloak

Un client es una aplicación que tiene permiso para pedirle tokens a Keycloak.
Este proyecto tiene dos clients registrados:

| Client | Tipo | Para qué |
|--------|------|---------|
| `market-ng` | Público (SPA) | La app Angular hace login aquí |
| `market-gateway` | Resource Server | El gateway valida tokens contra este client |

El tipo **público** significa que no tiene contraseña secreta — es para apps de navegador donde guardar un secret sería inseguro.

### 3. Roles — lo que puede hacer cada tipo de usuario

Los roles definen los permisos. Este proyecto tiene tres:

| Rol | Descripción |
|-----|-------------|
| `ROLE_ADMIN` | Acceso total: POS, ventas, existencias, artículos, rubros |
| `ROLE_SUPERVISOR` | Ventas, existencias, artículos (solo lectura en rubros, no puede borrar) |
| `ROLE_CAJERO` | Solo POS y ver ventas/existencias. No toca artículos ni rubros |

### 4. JWT (Token) — la credencial que viaja en cada request

Cuando un usuario se loguea exitosamente, Keycloak emite un **Access Token** en formato JWT.
Un JWT es un texto codificado en Base64 con tres partes: `header.payload.firma`.

El payload contiene información del usuario, por ejemplo:

```json
{
  "preferred_username": "cajero",
  "roles": ["ROLE_CAJERO"],
  "exp": 1234567890,
  "iss": "http://localhost:41880/realms/novamarket"
}
```

- `preferred_username` → nombre del usuario
- `roles` → qué puede hacer (viene del mapper configurado en Keycloak)
- `exp` → cuándo vence el token (unix timestamp)
- `iss` → quién emitió el token (el realm de Keycloak)

Este token **no se guarda en ningún servidor tuyo** — vive en memoria en Angular y viaja en el header HTTP.

### 5. Protocol Mapper — cómo llegan los roles al token

Por defecto Keycloak guarda los roles en `realm_access.roles`. Pero Angular lee `tokenParsed['roles']`.
Por eso hay un mapper llamado `realm-roles-claim` en `novamarket-realm.json` que copia los roles al claim `roles` directamente:

```json
{
  "name": "realm-roles-claim",
  "config": {
    "claim.name": "roles",
    "access.token.claim": "true"
  }
}
```

Sin ese mapper, `auth.service.ts` recibiría `roles = []` siempre.

---

## Los usuarios demo del proyecto

Están definidos en `realm/novamarket-realm.json` y se importan automáticamente al levantar Docker:

| Usuario | Contraseña | Rol asignado |
|---------|------------|--------------|
| `admin` | `admin123` | ROLE_ADMIN |
| `cajero` | `cajero123` | ROLE_CAJERO |
| `supervisor` | `supervisor123` | ROLE_SUPERVISOR |

---

## El flujo completo de login paso a paso

```
[1] Usuario abre http://localhost:4200
        │
        ▼
[2] Angular inicializa Keycloak (check-sso)
    ¿Ya hay sesión activa?
    ├── SÍ → recupera token silenciosamente → va a [6]
    └── NO → muestra pantalla /auth
        │
        ▼
[3] Usuario hace click en "Entrar con Keycloak"
    Angular llama a auth.login()
    Redirige al navegador a:
    http://localhost:41880/realms/novamarket/protocol/openid-connect/auth
        │
        ▼
[4] Usuario escribe usuario y contraseña EN Keycloak
    (tu app Angular NUNCA ve la contraseña)
        │
        ▼
[5] Keycloak valida las credenciales
    Genera un "código de autorización" y redirige a:
    http://localhost:4200/pos?code=XXXX  (PKCE)
        │
        ▼
[6] keycloak-js intercepta ese código
    Lo intercambia automáticamente por el Access Token JWT
    Llama a syncFromKeycloak():
      - Guarda el token en memoria
      - Extrae preferred_username
      - Extrae roles[]
        │
        ▼
[7] resolvePermissions(roles) determina qué puede ver el usuario
    defaultRoute(permissions) lo lleva a la pantalla correcta:
      - ADMIN    → /pos
      - CAJERO   → /pos
      - SUPERVISOR → /ventas
        │
        ▼
[8] Cada llamada HTTP a los microservicios:
    authInterceptor añade automáticamente:
    Authorization: Bearer <token JWT>
        │
        ▼
[9] El microservicio (ms-articulo, gateway) recibe el token
    Verifica la FIRMA del JWT con la clave pública de Keycloak
    (sin llamar a Keycloak — es local y rápido)
    Si el token es válido → procesa la request
    Si no → devuelve 401 Unauthorized
```

---

## Qué hace cada archivo del proyecto

### `keycloak/realm/novamarket-realm.json`
Es la configuración completa del realm exportada como JSON. Al levantar Docker con `--import-realm`, Keycloak lee este archivo y configura todo automáticamente: realm, clients, roles, usuarios demo, y mappers. Es la "fuente de verdad" de Keycloak para este proyecto.

### `keycloak/compose-dev.yml`
Levanta dos contenedores Docker para desarrollo:
- **PostgreSQL** → base de datos donde Keycloak guarda usuarios, sesiones y configuración
- **Keycloak** → el servidor de identidad, expuesto en el puerto `41880`

### `keycloak/compose.yml`
Lo mismo pero para producción, usando variables de entorno desde `.env` y expuesto en el puerto `28180`.

### `keycloak/Dockerfile.dev`
Construye la imagen de Keycloak con una fase de build optimizada (`kc.sh build`). Esto acelera el arranque porque pre-compila la configuración.

### `keycloak/start-dev.ps1`
Script PowerShell que:
1. Crea la red Docker `market-dev-net`
2. Construye la imagen
3. Levanta los contenedores
4. Espera hasta que Keycloak responda en el puerto 41880

---

## Qué hace cada archivo de Angular

### `core/auth/auth.service.ts`
El núcleo de la integración. Usa la librería `keycloak-js`.

```
init()                → conecta con Keycloak al arrancar la app
login()               → redirige al usuario a la pantalla de login de Keycloak
logout()              → cierra sesión en Keycloak y en la app
updateTokenIfNeeded() → refresca el JWT antes de que venza (mínimo 30s de vida)
syncFromKeycloak()    → extrae token + username + roles del JWT y los guarda como signals
hasRole(role)         → verifica si el usuario tiene un rol específico
```

Los datos del usuario (`token`, `username`, `roles`) son **signals de Angular** — se actualizan reactivamente y los componentes que los usen se re-renderizan automáticamente.

### `core/auth/auth.interceptor.ts`
Se ejecuta automáticamente **antes de cada HTTP request**. Su trabajo:
1. Verifica que el usuario está autenticado
2. Refresca el token si está por vencer
3. Agrega `Authorization: Bearer <token>` al header

Gracias a este interceptor, ningún servicio de Angular necesita manejar el token manualmente.

### `core/auth/auth.guard.ts` — `authGuard`
Protege rutas que requieren estar autenticado. Si el usuario no está logueado, lo manda a `/auth` con el `returnUrl` para volver después del login.

### `core/auth/role.guard.ts` — guards por pantalla
Cada pantalla tiene su propio guard:

| Guard | Pantalla protegida | Permiso requerido |
|-------|--------------------|-------------------|
| `posGuard` | `/pos` | `canAccessPos` |
| `ventasGuard` | `/ventas` | `canAccessVentas` |
| `existenciasGuard` | `/existencias` | `canAccessExistencias` |
| `articulosGuard` | `/articulos` | `canAccessArticulos` |
| `rubrosGuard` | `/rubros` | `canAccessRubros` |

Si el usuario no tiene el permiso para esa pantalla, lo redirige a la primera pantalla que sí puede ver (usando `defaultRoute`). Si no puede ver ninguna, va a `/auth?sinAcceso=1`.

### `core/auth/role-access.service.ts`
Servicio que usa `computed()` de Angular para calcular reactivamente los permisos y el rol del usuario actual. Los componentes inyectan este servicio para saber qué mostrar/ocultar.

```typescript
roles.permissions()  // { canAccessPos: true, canEditArticulos: false, ... }
roles.roleLabel()    // "Cajero"
roles.roleClass()    // "role-cajero"  (para estilos CSS)
roles.homeRoute()    // "/pos"
roles.can(p => p.canEditArticulos)  // false
```

### `core/auth/role-permissions.util.ts`
Define la tabla de permisos por rol. Es donde se decide qué puede hacer cada tipo de usuario:

```
ROLE_ADMIN     → todo habilitado
ROLE_SUPERVISOR → todo menos POS, no puede borrar artículos ni editar rubros
ROLE_CAJERO    → solo POS, ver ventas y existencias
Sin rol        → nada habilitado
```

### `app/app.routes.ts`
Aplica los guards a cada ruta. Ejemplo: `/articulos` tiene `canActivate: [articulosGuard]`, por lo que solo ADMIN y SUPERVISOR pueden acceder.

### `app/auth/auth.ts`
La página de login visible para el usuario. Maneja tres casos:
- Usuario no logueado → muestra botón "Entrar con Keycloak"
- Usuario logueado sin acceso → muestra mensaje de error `sinAcceso`
- Usuario logueado con acceso → redirige automáticamente a su pantalla

---

## Cómo levantar Keycloak en DEV

```powershell
# Desde la raíz del proyecto
docker network create market-dev-net
cd keycloak
.\start-dev.ps1
```

Una vez listo:
- **Admin Console:** http://localhost:41880/admin → usuario `admin` / contraseña `admin`
- **Realm:** http://localhost:41880/realms/novamarket
- **App Angular:** http://localhost:4200

---

## Cómo verificar que el JWT funciona (manual)

**Paso 1 — Obtener un token:**
```powershell
curl -s -X POST "http://localhost:41880/realms/novamarket/protocol/openid-connect/token" `
  -H "Content-Type: application/x-www-form-urlencoded" `
  -d "grant_type=password&client_id=market-ng&username=cajero&password=cajero123"
```

Copia el valor de `access_token` de la respuesta.

**Paso 2 — Inspeccionar el token:**
Pégalo en https://jwt.io y verifica en el payload:
```json
{
  "preferred_username": "cajero",
  "roles": ["ROLE_CAJERO"],
  "iss": "http://localhost:41880/realms/novamarket"
}
```

**Paso 3 — Llamar a un microservicio con el token:**
```powershell
curl -H "Authorization: Bearer <token>" http://localhost:18080/api/v1/productos/detalle/1
```
- Con token válido → `200 OK`
- Sin token → `401 Unauthorized`
- Con token de cajero en ruta de admin → `403 Forbidden`

---

## Lo que NO hace Angular con Keycloak

Es importante entender los límites:

- Angular **no valida** el JWT — solo lo usa. La validación real la hacen los microservicios.
- Angular **no guarda** el token en `localStorage` — vive en memoria (más seguro).
- Angular **no conoce la contraseña** del usuario — eso lo maneja Keycloak directamente.
- Los guards de Angular son **solo UI** — un usuario malicioso podría saltarlos. La seguridad real está en los microservicios (401/403 desde el backend).

---

## PROD

```powershell
cd keycloak
copy .env.example .env
# Edita .env con contraseñas seguras
docker compose up -d --build
```

Disponible en http://localhost:28180

Configura el backend con:
```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://keycloak:8080/realms/novamarket
```
