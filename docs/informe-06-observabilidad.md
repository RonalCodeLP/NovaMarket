# 6. Observabilidad — NovaMarket (DEV)

## 6.1 Métricas

### Modelo general

El stack usa **Micrometer** integrado con **Spring Boot Actuator** y el registro **micrometer-registry-prometheus**. Cada componente expone un endpoint HTTP en formato texto compatible con Prometheus; el servidor **Prometheus** hace *scraping* periódico (cada 15 s en DEV) y **Grafana** consulta Prometheus como datasource para gráficos y tablas.

**Nota:** el **API Gateway** (Spring Cloud Gateway, WebFlux) publica métricas propias (`spring_cloud_gateway_*`, `http_client_*`). Los microservicios servlet publican `http_server_requests_*` y métricas JVM estándar.

### Tabla de capas

| Capa | Herramienta | Puerto DEV | Rol |
|------|-------------|------------|-----|
| Exportación | Actuator + `micrometer-registry-prometheus` | Ver tabla de servicios | Publica `/actuator/prometheus` en cada instancia |
| Recolección | Prometheus (`obs/prometheus/prometheus-dev.yml`) | http://localhost:19090 | Almacena series temporales; UI en `/targets` y `/graph` |
| Visualización | Grafana (datasource Prometheus provisionado) | http://localhost:13000 | Dashboard **NovaMarket DEV** y Explore (`admin` / `admin`) |
| Kafka (métricas de broker) | kafka-exporter | http://localhost:41308/metrics | Job `kafka-exporter-dev` en Prometheus |

### Endpoints y puertos de exportación (DEV)

| Componente | Puerto | Endpoint de métricas |
|------------|--------|----------------------|
| Gateway | 18080 | `/actuator/prometheus` |
| ms-rubro | 8081 | `/actuator/prometheus` |
| ms-articulo | 9091 | `/actuator/prometheus` |
| ms-venta | 19051 | `/actuator/prometheus` |
| ms-pago | 19061 | `/actuator/prometheus` |
| Prometheus | 19090 | — |
| Grafana | 13000 | — |

### Consultas PromQL de referencia

| Objetivo | Consulta |
|----------|----------|
| Servicio vivo | `up{job="ms-venta-dev"}` |
| Gateway por ruta | `spring_cloud_gateway_requests_seconds_count{job="gateway-dev"}` |
| HTTP en microservicio | `http_server_requests_seconds_count{job="ms-articulo-dev"}` |
| Memoria JVM | `jvm_memory_used_bytes{job="ms-pago-dev"}` |
| Kafka | `kafka_consumergroup_lag` (con kafka-exporter UP) |

Configuración: `obs/compose-dev.yml`, `obs/prometheus/prometheus-dev.yml`, `infra/config-repo/*-dev.yml` (exposición `management.endpoints`).

---

## 6.2 Logs

### Centralización en Loki

Los logs **no** salen directamente de la aplicación hacia Loki. El flujo es:

```text
Microservicio (Logback)
    → archivo local rolling (*.log en carpeta logs/)
         → Promtail (contenedor Docker, obs/promtail/config.yml)
              → push HTTP a Loki (:3100 en red Docker)
                   → Grafana (datasource Loki) consulta con LogQL
```

1. **Generación:** cada servicio usa `logback-spring.xml` con appenders **CONSOLE** y **FILE** (rotación por tamaño/tiempo). Los archivos quedan en rutas como `services/ms-venta/logs/`, `infra/gateway/logs/`, etc.

2. **Recolección:** **Promtail** (`market-promtail-dev`) monta esas carpetas como volúmenes de solo lectura (ver `obs/compose-dev.yml`) y las etiqueta con labels `service` y `job` (por ejemplo `service=ms-venta`).

3. **Almacenamiento:** **Loki** (`market-loki-dev`, puerto host **13100**) recibe los streams vía `http://loki:3100/loki/api/v1/push`.

4. **Consulta:** Grafana tiene el datasource **Loki** provisionado (`obs/grafana/provisioning/datasources/datasources.yml`). Ejemplo LogQL: `{service="ms-venta"}`.

**Requisito operativo:** los servicios deben ejecutarse (Maven) para que existan archivos `.log`; si la carpeta está vacía, Loki no mostrará líneas para ese servicio.

**Cobertura DEV actual de Promtail:** gateway, ms-rubro, ms-articulo, ms-venta, ms-pago.

---

## 6.3 Alertas

En el entorno DEV del repositorio **no hay reglas de alerta desplegadas** (no existe configuración de Alertmanager ni `alerting` en Prometheus). La observabilidad se limita a consulta manual y dashboard.

A continuación, alertas **recomendadas** para una fase posterior (Grafana Alerting o Alertmanager):

| Alerta | Condición (ejemplo PromQL / LogQL) | Qué detecta |
|--------|-----------------------------------|-------------|
| Servicio caído | `up{job="gateway-dev"} == 0` durante 2m | Gateway no responde al scrape |
| Microservicio caído | `up{job=~"ms-.*"} == 0` durante 2m | Instancia no registrada en Prometheus |
| Errores HTTP gateway | `increase(spring_cloud_gateway_requests_seconds_count{status=~"5.."}[5m]) > 0` | Respuestas 5xx en rutas del gateway |
| Errores HTTP ventas | `increase(http_server_requests_seconds_count{job="ms-venta-dev",status="500"}[5m]) > 0` | Fallos en API de ventas |
| Latencia alta ventas | `histogram_quantile(0.95, rate(http_server_requests_seconds_bucket{job="ms-venta-dev"}[5m])) > 2` | p95 de latencia > 2 s (ajustar umbral) |
| Kafka consumer lag | `kafka_consumergroup_lag > 100` | Consumidor retrasado respecto al topic |
| Disco / JVM crítica | `jvm_memory_used_bytes / jvm_memory_max_bytes > 0.9` | Uso de heap > 90 % |
| Log de error | `{service="ms-venta"} \|= "ERROR"` (Loki) | Aparición de líneas ERROR en logs de venta |

Estado en informe: **alertas definidas en código/config: No** (diseño documentado; implementación pendiente).

---

## 6.4 Matriz de observabilidad

Estado según configuración en `obs/` y `infra/config-repo` (DEV). “Sí” implica componente configurado y funcional **si el servicio está levantado** y Prometheus/Grafana/Loki están activos.

| Microservicio / componente | UP en Prometheus | Requests visibles | Errores visibles | Logs en Loki | Alerta definida |
|--------------------------|------------------|-------------------|------------------|--------------|-----------------|
| Gateway | Sí (`gateway-dev`, :18080) | Sí (`spring_cloud_gateway_requests_*`) | Sí (labels `status`, `outcome`) | Sí (`service=gateway`) | No |
| ms-rubro | Sí (`ms-rubro-dev`, :8081) | Sí | Sí | Sí (`service=ms-rubro`) | No |
| ms-articulo | Sí (`ms-articulo-dev`, :9091) | Sí | Sí | Sí (`service=ms-articulo`) | No |
| ms-venta | Sí (`ms-venta-dev`, :19051) | Sí | Sí | Sí (`service=ms-venta`) | No |
| ms-pago | Sí (`ms-pago-dev`, :19061) | Sí | Sí | Sí (`service=ms-pago`) | No |
| Kafka (exporter) | Sí (`kafka-exporter-dev`) | N/A (métricas de broker/grupos) | N/A | No | No |

### Cómo validar cada columna

| Columna | Validación |
|---------|------------|
| UP | http://localhost:19090/targets → estado **UP** |
| Requests | Grafana Explore → Prometheus → consultas de la sección 6.1 |
| Errores | Mismas métricas filtrando `status=~"4.."` o `5.."`; logs `{service="..."} \|= "ERROR"` |
| Logs Loki | Grafana Explore → Loki → `{service="ms-venta"}` |
| Alerta | No aplica hasta implementar reglas (sección 6.3) |

### Despliegue del stack (referencia)

```powershell
docker network create market-dev-net
cd obs
docker compose -f compose-dev.yml up -d
```

Guías del proyecto: `obs/GRAFANA-DEV.md`, `obs/prometheus/QUERIES-DEV.md`, `OBS-KAFKA-DEV.md`.
