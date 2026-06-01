# Consultas Prometheus (DEV) — NovaMarket

## Antes de consultar

1. Targets en verde: http://localhost:19090/targets  
2. Si `gateway-dev` sigue en **7091**, reinicia Prometheus:

   ```powershell
   cd C:\ms1\NovaMarket\obs
   docker compose -f compose-dev.yml up -d --force-recreate prometheus
   ```

---

## Métrica `up` (¿está vivo el servicio?)

```promql
up
```

```promql
up{job="gateway-dev"}
```

`1` = UP, `0` = DOWN.

---

## Gateway (Spring Cloud Gateway — **no** usa `http_server_requests`)

El gateway es **reactivo**. Usa estas métricas:

| Qué quieres ver | Consulta |
|-----------------|----------|
| Peticiones por ruta | `spring_cloud_gateway_requests_seconds_count{job="gateway-dev"}` |
| Solo ruta de ventas | `spring_cloud_gateway_requests_seconds_count{job="gateway-dev",routeId="venta-route"}` |
| Errores 5xx en ventas | `spring_cloud_gateway_requests_seconds_count{job="gateway-dev",routeId="venta-route",status=~"5.."}` |
| Salidas HTTP a microservicios | `http_client_requests_seconds_count{job="gateway-dev"}` |

**No uses** en el gateway: `http_server_requests_seconds_count` → saldrá vacío (no existe).

---

## Microservicios servlet (ms-articulo, ms-venta, ms-pago, ms-rubro)

```promql
http_server_requests_seconds_count{job="ms-articulo-dev"}
```

```promql
http_server_requests_seconds_count{job="ms-venta-dev",uri="/api/v1/ventas"}
```

---

## Kafka

```promql
kafka_consumergroup_lag
```

(Requiere `kafka-exporter-dev` UP.)

---

## JVM (cualquier job)

```promql
jvm_memory_used_bytes{job="ms-venta-dev"}
```

---

## Generar datos

Usa el POS o:

```powershell
curl http://localhost:18080/actuator/health
```

Espera ~15 s (`scrape_interval`) y vuelve a ejecutar la consulta.
