# Referencia de puertos

## Infraestructura

| Componente | DEV (Docker) | PROD (Docker) | Puerto interno contenedor |
|------------|-------------:|--------------:|--------------------------:|
| Config Server | 18888 | 28888 | 8888 (prod) / 18888 (dev) |
| Eureka | 18761 | 28761 | 8761 (prod) / 18761 (dev) |
| Gateway | 18080 | 28082 | 8080 (prod) / 18080 (dev) |
| Keycloak | 41880 | 28180 | 8080 |
| Postgres Keycloak | — | 25437 | 5432 |

---

## Microservicios (DEV directo)

| Servicio | Puerto DEV | Notas |
|----------|------------|--------|
| ms-rubro | 8081 | |
| ms-articulo | 9091 | Circuit breaker, JWT |
| ms-venta | 19051 | Productor Kafka |
| ms-pago | 19061 | Consumidor Kafka |

En PROD los MS se consumen **vía gateway** (28082).

---

## PostgreSQL DEV (host)

| Servicio | Puerto host |
|----------|------------:|
| ms-rubro | 15432 |
| ms-articulo | 15433 |
| ms-venta | 15434 |
| ms-pago | 15435 |

---

## Kafka

| Servicio | DEV | PROD |
|----------|----:|-----:|
| Broker | 41092 | 28092 |
| Kafka UI | 41085 | 28085 |
| Exporter | 41308 | 29308 |

---

## Observabilidad

| Servicio | DEV | PROD |
|----------|----:|-----:|
| Prometheus | 19090 | 29090 |
| Loki | 13100 | 23100 |
| Grafana | 13000 | 23000 |

---

## Frontend

| App | URL DEV |
|-----|---------|
| market-ng | http://localhost:4200 |

PROD build: API `28082`, Keycloak `28180` (`environment.prod.ts`).

---

## Redes Docker

| Red | Uso |
|-----|-----|
| `market-dev-net` | Keycloak, Kafka, obs DEV |
| `market-prod-net` | Infra PROD, Keycloak PROD, Kafka PROD |
