# Probar Kafka + Prometheus + Grafana (DEV)

## Qué vas a ver

| Herramienta | URL | Para qué |
|-------------|-----|----------|
| **Kafka UI** | http://localhost:41085 | Topics, mensajes, consumidores |
| **Prometheus** | http://localhost:19090 | Métricas crudas, estado de targets |
| **Grafana** | http://localhost:13000 | Dashboards (user: `admin` / pass: `admin`) — guía: [obs/GRAFANA-DEV.md](obs/GRAFANA-DEV.md) |
| **Loki** (vía Grafana) | datasource Loki | Logs centralizados |

Flujo de negocio con Kafka:

```text
Caja (venta) → ms-venta publica en topic "orden-eventos"
            → ms-pago consume y registra pago (si no hubo pago sync antes)
            → ms-pago puede publicar en "pago-eventos"
```

---

## 1. Red Docker (una vez)

```powershell
docker network create market-dev-net
```

---

## 2. Levantar Kafka

```powershell
cd C:\ms1\NovaMarket\kafka
docker compose -f compose-dev.yml up -d
```

Comprobar:

- http://localhost:41085 — Kafka UI (cluster `dev`)
- Exporter métricas: puerto **41308**

---

## 3. Levantar observabilidad (Prometheus + Loki + Grafana)

```powershell
cd C:\ms1\NovaMarket\obs
docker compose -f compose-dev.yml up -d
```

Comprobar:

- http://localhost:19090 — Prometheus
- http://localhost:13000 — Grafana (`admin` / `admin`)

Reinicia Prometheus si ya estaba arriba antes de editar `prometheus-dev.yml`:

```powershell
docker restart market-prometheus-dev
```

---

## 4. Levantar aplicación (Maven)

**Infra** (3 terminales):

```powershell
cd infra\config-server   ; mvn spring-boot:run
cd infra\registry-server ; mvn spring-boot:run
cd infra\gateway         ; mvn spring-boot:run
```

**Postgres** (si no están):

```powershell
cd services\ms-auth      ; docker compose -f compose-dev.yml up -d
cd services\ms-rubro     ; docker compose -f compose-dev.yml up -d
cd services\ms-articulo  ; docker compose -f compose-dev.yml up -d
cd services\ms-venta     ; docker compose -f compose-dev.yml up -d
cd services\ms-pago      ; docker compose -f compose-dev.yml up -d
```

**Microservicios** (Kafka obligatorio en **ms-venta** y **ms-pago**):

```powershell
cd services\ms-auth      ; mvn spring-boot:run
cd services\ms-rubro     ; mvn spring-boot:run
cd services\ms-articulo  ; mvn spring-boot:run
cd services\ms-venta     ; mvn spring-boot:run
cd services\ms-pago      ; mvn spring-boot:run
```

Puertos que Prometheus espera (config-repo DEV):

| Servicio | Puerto | Métricas |
|----------|--------|----------|
| gateway | 18080 | http://localhost:18080/actuator/prometheus |
| ms-auth | 8041 | http://localhost:8041/actuator/prometheus |
| ms-rubro | 8081 | http://localhost:8081/actuator/prometheus |
| ms-articulo | 9091 | http://localhost:9091/actuator/prometheus |
| ms-venta | 19051 | http://localhost:19051/actuator/prometheus |
| ms-pago | 19061 | http://localhost:19061/actuator/prometheus |

---

## 5. Generar tráfico Kafka

1. Frontend: http://localhost:4200 — login `cajero` / `cajero123`
2. Crea rubros y artículos (con stock).
3. **Caja** → agrega productos → **Confirmar pago y boleta**.

En **ms-venta** consola deberías ver publicación a Kafka (`orden-eventos`).  
En **ms-pago** consola: consumo del evento (o `skipped` si el pago ya se registró por API).

---

## 6. Ver Kafka (Kafka UI)

1. Abre http://localhost:41085
2. Menú **Topics** → busca `orden-eventos` (y `pago-eventos` si aplica).
3. Entra al topic → **Messages** → ver JSON del evento de venta.

---

## 7. Ver Prometheus

1. http://localhost:19090/targets
2. Targets en **UP** (verde): `gateway-dev`, `ms-venta-dev`, `ms-pago-dev`, `kafka-exporter-dev`, etc.
3. Si **DOWN**: el microservicio no está en ese puerto o no expone `/actuator/prometheus`.

Consultas útiles en **Graph** (copiar de `obs/prometheus/QUERIES-DEV.md`):

```promql
up
spring_cloud_gateway_requests_seconds_count{job="gateway-dev"}
http_server_requests_seconds_count{job="ms-venta-dev"}
kafka_consumergroup_lag
```

> En el **gateway** no existe `http_server_requests_*` (es WebFlux). Usa `spring_cloud_gateway_requests_seconds_count`.

---

## 8. Ver todo en Grafana

1. http://localhost:13000 — login `admin` / `admin`
2. **Connections** → datasources: Prometheus (default) y Loki.
3. **Explore** → Prometheus → consulta `up` o `jvm_memory_used_bytes`.
4. **Explore** → Loki → consulta logs (si Promtail lee carpetas `services/ms-*/logs`):

```logql
{service="ms-venta"}
```

5. Para Kafka: en Explore/Prometheus busca métricas con prefijo `kafka_` del exporter.

---

## Problemas frecuentes

| Síntoma | Solución |
|---------|----------|
| Targets DOWN en Prometheus | Microservicio apagado o puerto distinto al de `config-repo/*-dev.yml` |
| Kafka UI vacío | Kafka no levantado o venta no hecha |
| ms-venta error Kafka | `localhost:41092` debe estar arriba (`market-kafka-dev`) |
| Grafana sin datos | `docker restart market-prometheus-dev` y espera 30 s |
| Logs vacíos en Loki | Ejecuta servicios Maven para generar `logs/*.log`; carpetas deben existir |

---

## Apagar

```powershell
cd kafka ; docker compose -f compose-dev.yml down
cd obs   ; docker compose -f compose-dev.yml down
```
