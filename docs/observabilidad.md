# Observabilidad

NovaMarket expone métricas con **Micrometer + Actuator + Prometheus** y centraliza logs con **Promtail → Loki → Grafana**.

---

## Capas

| Capa | Herramienta | DEV |
|------|-------------|-----|
| Exportación | Actuator `/actuator/prometheus` | Por servicio |
| Métricas | Prometheus | http://localhost:19090 |
| Logs | Loki + Promtail | vía Grafana |
| Visualización | Grafana | http://localhost:13000 |

---

## Arranque stack DEV

```powershell
docker network create market-dev-net
cd obs
docker compose -f compose-dev.yml up -d
```

---

## Endpoints Actuator (por servicio)

| Endpoint | Uso |
|----------|-----|
| `/actuator/health` | Salud (público) |
| `/actuator/prometheus` | Scraping Prometheus |
| `/actuator/circuitbreakers` | ms-articulo — estado CB |
| `/actuator/circuitbreakerevents` | Eventos Resilience4j |

---

## Prometheus

Targets: http://localhost:19090/targets

Jobs DEV: `gateway-dev`, `ms-articulo-dev`, `ms-rubro-dev`, `ms-venta-dev`, `ms-pago-dev`, `kafka-exporter-dev`

Consultas útiles:

```promql
up
sum by (job) (rate(http_server_requests_seconds_count[1m]))
spring_cloud_gateway_requests_seconds_count{job="gateway-dev"}
jvm_memory_used_bytes{job="ms-venta-dev"}
```

> El gateway (WebFlux) usa `spring_cloud_gateway_*`, no `http_server_requests_*`.

---

## Loki (LogQL)

Labels Promtail: `service=ms-articulo`, `ms-rubro`, `ms-venta`, `ms-pago`, `gateway`

```logql
{service="ms-articulo"} |= "[PRODUCTO]"
{service=~"gateway|ms-articulo|ms-rubro"}
```

Requisito: archivos `logs/*.log` generados al correr MS con Maven.

---

## Grafana

Dashboard provisionado: carpeta **NovaMarket** → **NovaMarket DEV**

Explore → datasource Prometheus o Loki.

Guía rápida: `obs/GRAFANA-DEV.md`

---

## Evidencias (curso)

1. Captura `/actuator/health` (gateway, articulo, rubro)  
2. Prometheus `up` en verde  
3. Grafana: tráfico HTTP o JVM  
4. Loki: logs de una petición `detalle/1`  
5. (Opcional) Circuit breaker en actuator + log fallback  

---

## PROD

```powershell
cd obs
docker compose up -d
```

Grafana: http://localhost:23000 — Prometheus: http://localhost:29090
