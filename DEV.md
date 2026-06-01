# Arranque DEV — NovaMarket

## 1. Una sola vez

```powershell
docker network create market-prod-net
docker network create market-dev-net
```

Si aún ves contenedores viejos con prefijo `ecom-` en Docker Desktop, detén y elimínalos, luego vuelve a levantar con los `compose` del repo (ahora usan prefijo `market-`):

```powershell
# Ejemplo por stack (desde cada carpeta)
docker compose -f compose-dev.yml down -v
docker compose -f compose-dev.yml up -d
```

## 2. Infraestructura (DEV = Maven, no hay `compose-dev.yml` en `infra/`)

En `infra/` solo existe **`compose.yml`** (modo PROD en Docker). Para DEV usa **Maven** en 3 terminales:

```powershell
cd infra/config-server   ; mvn spring-boot:run   # :18888
cd infra/registry-server ; mvn spring-boot:run   # :18761
cd infra/gateway         ; mvn spring-boot:run   # :18080
```

Comprobar: http://localhost:18080/actuator/health

**Alternativa:** infra en Docker (puertos PROD, no los de DEV):

```powershell
cd infra
docker network create market-prod-net
docker compose up -d --build
# Config :28888, Eureka :28761, Gateway :28082
```

## 3. PostgreSQL (Docker)

```powershell
cd services/ms-auth      ; docker compose -f compose-dev.yml up -d   # :15431
cd services/ms-rubro     ; docker compose -f compose-dev.yml up -d   # :15432
cd services/ms-articulo  ; docker compose -f compose-dev.yml up -d   # :15433
cd services/ms-venta     ; docker compose -f compose-dev.yml up -d   # :15434
cd services/ms-cliente   ; docker compose -f compose-dev.yml up -d   # :15436
cd services/ms-pago      ; docker compose -f compose-dev.yml up -d   # :15435
```

Guía detallada Kafka + Prometheus + Grafana: **[OBS-KAFKA-DEV.md](OBS-KAFKA-DEV.md)**

## 4. Kafka (opcional; ventas funcionan sin Kafka si el pago es síncrono)

```powershell
cd kafka ; docker compose -f compose-dev.yml up -d   # :41092
```

## 5. Microservicios (1 terminal cada uno)

```powershell
cd services/ms-auth      ; mvn spring-boot:run
cd services/ms-rubro     ; mvn spring-boot:run
cd services/ms-articulo  ; mvn spring-boot:run
cd services/ms-cliente   ; mvn spring-boot:run
cd services/ms-venta     ; mvn spring-boot:run
cd services/ms-pago      ; mvn spring-boot:run
```

En Eureka (http://localhost:18761) deben aparecer: **MS-AUTH**, **MS-RUBRO**, **MS-ARTICULO**, **MS-CLIENTE**, **MS-VENTA**, **MS-PAGO**.

## 6. Frontend

```powershell
cd clients/market-ng
npm install
ng serve
```

http://localhost:4200 — login: `cajero` / `cajero123`

## Orden mínimo para probar caja

1. Infra (config + eureka + gateway)  
2. Postgres: ms-auth, ms-rubro, ms-articulo, ms-venta, ms-pago  
3. Maven: ms-auth, ms-rubro, ms-articulo, ms-venta, ms-pago  
4. Angular  

Crear al menos un **rubro** y un **artículo** con código de barras y stock antes de usar **Caja**.
