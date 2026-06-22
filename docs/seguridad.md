# Seguridad

NovaMarket usa **Keycloak** como proveedor de identidad (IdP) y **OAuth2 / OpenID Connect** para autenticación. Los microservicios actúan como **OAuth2 Resource Servers** y validan el JWT en el claim estándar `Authorization: Bearer`.

---

## Componentes

| Componente | Rol |
|------------|-----|
| **Keycloak** | Login, usuarios, roles, emisión de JWT (RS256) |
| **Gateway** | Borde seguro: valida JWT en rutas protegidas |
| **ms-articulo** | Valida JWT localmente; autorización por rol en escrituras |
| **market-ng** | Cliente público OIDC (`market-ng`) con PKCE |

---

## Realm `novamarket`

| Elemento | Valor |
|----------|--------|
| Cliente SPA | `market-ng` |
| Redirect DEV | `http://localhost:4200/*` |
| Claim de roles | `roles` (ej. `ROLE_CAJERO`, `ROLE_ADMIN`) |
| Access token | 3600 s (1 h); renovación vía `keycloak-js` |

### Usuarios demo

| Usuario | Contraseña | Rol |
|---------|------------|-----|
| admin | admin123 | ROLE_ADMIN |
| cajero | cajero123 | ROLE_CAJERO |
| supervisor | supervisor123 | ROLE_SUPERVISOR |

---

## Flujo OIDC (Angular)

```text
1. Usuario → "Entrar con Keycloak"
2. Redirect a Keycloak login
3. Keycloak → redirect a localhost:4200 con code (PKCE)
4. keycloak-js obtiene access_token
5. Interceptor HTTP añade Bearer a llamadas al gateway
```

---

## Configuración backend

**Gateway DEV** (`infra/config-repo/gateway-dev.yml`):

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://localhost:41880/realms/novamarket
```

**Gateway PROD:**

```yaml
issuer-uri: ${KEYCLOAK_ISSUER_URI:http://keycloak:8080/realms/novamarket}
```

**ms-articulo:** mismo `issuer-uri` en `ms-articulo-{dev,prod}.yml`.

El mapper `realm-roles-claim` en `keycloak/realm/novamarket-realm.json` expone roles compatibles con Spring Security (`authoritiesClaimName: roles`).

---

## Autorización por rol

En **ms-articulo**:

| Método | Ruta | Requisito |
|--------|------|-----------|
| GET | `/api/v1/productos/**` | Autenticado |
| POST/PUT/DELETE | `/api/v1/productos/**` | Rol **ADMIN** |

En **gateway**, rutas públicas (sin JWT) incluyen actualmente categorías, ventas y pagos para facilitar el POS — revisar `SecurityConfig` del gateway si se endurece la política.

---

## Probar token manualmente

```powershell
curl -s -X POST "http://localhost:41880/realms/novamarket/protocol/openid-connect/token" `
  -H "Content-Type: application/x-www-form-urlencoded" `
  -d "grant_type=password" `
  -d "client_id=market-ng" `
  -d "username=cajero" `
  -d "password=cajero123"
```

Usar `access_token` en:

```http
GET http://localhost:18080/api/v1/productos/detalle/1
Authorization: Bearer <token>
```

---

## Evidencias (curso)

1. Login Keycloak + captura Angular  
2. jwt.io con claims `iss`, `roles`, `exp`  
3. 401 sin token / 200 con token  
4. 403 cajero vs admin en POST producto  

Ver también material histórico en `docs/sesiones/s06-seguridad.md`.
