# Kafka y eventos

Arquitectura **orientada a eventos** para desacoplar notificación de ventas y procesamiento asíncrono de pagos.

---

## Tópicos

| Tópico | Productor | Consumidor | Evento |
|--------|-----------|------------|--------|
| `orden-eventos` | ms-venta | ms-pago (`ms-pago-group`) | `orden.creada` |
| `pago-eventos` | ms-pago | *(extensión futura)* | `pago.aprobado` / `pago.rechazado` |

Formato: JSON (`EventoOrden`, `EventoPago`). Clave: `ordenId`.

---

## Flujo híbrido (POS)

1. **Síncrono:** ms-venta → Feign → ms-pago (`POST /api/v1/pagos/registrar`) — respuesta inmediata al cajero.  
2. **Asíncrono:** ms-venta publica `orden.creada` en Kafka.  
3. ms-pago consume; si el pago ya existió por Feign → **skip** (idempotencia).  
4. Si no existía (API legacy `/api/v1/ordenes`) → procesa y publica `pago-eventos`.

---

## Arranque DEV

```powershell
docker network create market-dev-net
cd kafka
docker compose -f compose-dev.yml up -d
```

| URL | Uso |
|-----|-----|
| http://localhost:41085 | Kafka UI |
| localhost:41092 | Bootstrap servers (apps Maven) |

Config MS: `spring.kafka.bootstrap-servers: localhost:41092`

---

## Probar eventos

**Opción A — POS:** venta en Angular → ver mensaje en Kafka UI → topic `orden-eventos`.

**Opción B — API legacy:**

```http
POST http://localhost:18080/api/v1/ordenes
Content-Type: application/json

{"usuarioId": 1, "total": 25.50, "detalles": []}
```

Logs ms-venta: `status=published` — ms-pago: `status=consumed` / `processed`.

---

## Observabilidad Kafka

- Exporter: puerto **41308** (DEV)  
- Prometheus job: `kafka-exporter-dev`  
- PromQL: `kafka_consumergroup_lag`

---

## PROD

```powershell
cd kafka
docker compose up -d
```

Broker host: **28092** — UI: **28085**

---

## Evidencias (curso)

1. Kafka UI con topic y mensaje JSON  
2. Logs productor (ms-venta) y consumidor (ms-pago)  
3. Diagrama secuencia venta → Kafka → pago  
4. (Opcional) métrica lag en Grafana  
