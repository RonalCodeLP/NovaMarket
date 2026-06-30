# market-ng — Frontend Angular

SPA Angular de la plataforma POS (cajas, rubros, artículos, ventas — multi-punto).

## Requisitos

- Node.js 20+, npm
- Keycloak DEV en http://localhost:41880
- Gateway DEV en http://localhost:18080

## Arranque

```powershell
cd clients/market-ng
npm install
ng serve
```

http://localhost:4200 → **Entrar con Keycloak**

## Configuración

| Archivo | Entorno |
|---------|---------|
| `src/environments/environment.ts` | DEV |
| `src/environments/environment.prod.ts` | PROD build |

Incluye `apiBaseUrl` y credenciales OIDC (`keycloak.url`, `realm`, `clientId`).

## Build producción

```powershell
ng build --configuration production
```

Salida: `dist/erpng/browser`

## Auth

- Librería: `keycloak-js` (PKCE)
- Servicios: `src/app/core/auth/`

Documentación: [docs/seguridad.md](../../docs/seguridad.md)
