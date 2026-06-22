# Keycloak — NovaMarket

Identidad **OAuth2/OIDC** — realm **`novamarket`**.

Documentación: [docs/seguridad.md](../docs/seguridad.md)

## DEV

```powershell
docker network create market-dev-net
cd keycloak
.\start-dev.ps1
```

- Admin: http://localhost:41880/admin (`admin`/`admin`)
- Cliente SPA: `market-ng`

## PROD

```powershell
docker network create market-prod-net
copy .env.example .env
docker compose up -d --build
```

- http://localhost:28180

## Realm

Import automático desde `realm/novamarket-realm.json` (usuarios demo, roles, mappers).
