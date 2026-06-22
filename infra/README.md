# Infraestructura — NovaMarket

Componentes compartidos: **Config Server**, **Eureka**, **API Gateway**.

Documentación completa: [docs/arquitectura.md](../docs/arquitectura.md) · [docs/desarrollo.md](../docs/desarrollo.md) · [docs/produccion.md](../docs/produccion.md)

## Módulos

| Carpeta | Puerto DEV | Puerto PROD | Función |
|---------|----------:|------------:|---------|
| `config-server/` | 18888 | 28888 | Config centralizada (`config-repo/`) |
| `registry-server/` | 18761 | 28761 | Eureka |
| `gateway/` | 18080 | 28082 | Entrada HTTP + JWT Keycloak |

## DEV (Docker)

```powershell
docker network create market-dev-net
cd infra
.\start-dev.ps1
```

Contenedores: `market-config-dev`, `market-eureka-dev`, `market-gateway-dev`  
Puertos: **18888**, **18761**, **18080**

Requisito previo: **Keycloak** en http://localhost:41880 (`keycloak/start-dev.ps1`)

### DEV alternativo (Maven, sin contenedores)

```powershell
cd config-server   ; mvn spring-boot:run
cd registry-server ; mvn spring-boot:run
cd gateway         ; mvn spring-boot:run
```

## PROD (Docker)

```powershell
docker network create market-prod-net
cd infra
docker compose up -d --build
```

Variables: ver `.env.example` (`KEYCLOAK_ISSUER_URI`).

## Config repo

Perfiles en `config-repo/`:

- `gateway-dev.yml` / `gateway-prod.yml`
- `ms-*-dev.yml` / `ms-*-prod.yml`

Seguridad: `issuer-uri` apunta al realm Keycloak `novamarket`.
