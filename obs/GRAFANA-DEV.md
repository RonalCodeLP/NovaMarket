# Probar Grafana (DEV) — NovaMarket

## Qué es Grafana aquí

**Grafana** es la pantalla de visualización: lee datos de **Prometheus** (métricas) y **Loki** (logs) y los muestra en gráficos y tablas.

| Componente | URL | Rol |
|------------|-----|-----|
| Grafana | http://localhost:13000 | UI (`admin` / `admin`) |
| Prometheus | http://localhost:19090 | Métricas (detrás, datasource) |
| Loki | http://localhost:13100 | Logs (detrás, datasource) |

---

## 1. Levantar el stack

```powershell
docker network create market-dev-net

cd C:\ms1\NovaMarket\obs
docker compose -f compose-dev.yml up -d
```

Comprueba contenedores:

```powershell
docker ps --filter "name=market-grafana-dev"
docker ps --filter "name=market-prometheus-dev"
docker ps --filter "name=market-loki-dev"
```

---

## 2. Entrar a Grafana

1. Abre **http://localhost:13000**
2. Usuario: `admin`
3. Contraseña: `admin` (te puede pedir cambiarla; puedes omitir en local)

Datasources ya provisionados: **Prometheus** (default) y **Loki**.

---

## 3. Dashboard incluido

Menú **Dashboards** → carpeta **NovaMarket** → **NovaMarket DEV**

Paneles:

- Estado **UP/DOWN** de cada job de Prometheus
- Peticiones/s del **gateway** por ruta
- Peticiones/s de **microservicios**
- Memoria JVM
- **Logs** de ms-venta, ms-pago y gateway

Si no aparece el dashboard, recrea Grafana:

```powershell
cd C:\ms1\NovaMarket\obs
docker compose -f compose-dev.yml up -d --force-recreate grafana
```

---

## 4. Generar datos para ver gráficos

1. Levanta **gateway** y microservicios en Maven (puertos DEV).
2. Usa el **POS** (login, ventas, listar artículos).
3. En el dashboard, arriba a la derecha: rango **Last 15 minutes** y **Refresh 10s**.

Sin tráfico, los gráficos de “peticiones/s” pueden estar en cero (es normal).

---

## 5. Explore (pruebas manuales)

### Métricas (Prometheus)

1. Menú **Explore** (icono brújula)
2. Datasource: **Prometheus**
3. Ejemplos:

```promql
up
spring_cloud_gateway_requests_seconds_count{job="gateway-dev"}
http_server_requests_seconds_count{job="ms-venta-dev"}
```

4. **Run query** → pestaña **Graph**

### Logs (Loki)

1. **Explore** → datasource **Loki**
2. Consultas:

```logql
{service="ms-venta"}
{service="ms-pago"}
{service="gateway"}
```

3. Necesitas archivos en `services/ms-*/logs/*.log` (los genera Maven al correr los servicios).

---

## 6. Crear tu primer panel (opcional)

1. **Dashboards** → **NovaMarket DEV** → **Add** → **Visualization**
2. Datasource **Prometheus**
3. Consulta: `up{job="gateway-dev"}`
4. Tipo **Stat** o **Time series**
5. **Save dashboard**

---

## Problemas frecuentes

| Síntoma | Solución |
|---------|----------|
| No carga http://localhost:13000 | `docker compose -f compose-dev.yml up -d` en `obs/` |
| “Datasource Prometheus error” | Prometheus caído: `docker ps` → `market-prometheus-dev` |
| Gráficos vacíos | Microservicios apagados o sin tráfico; revisa http://localhost:19090/targets |
| Logs vacíos en Loki | Ejecuta ms-venta/ms-pago con Maven; revisa que exista `services/ms-venta/logs/` |
| Login olvidado | `admin` / `admin` (o el que hayas puesto tras el primer login) |

---

## Apagar

```powershell
cd C:\ms1\NovaMarket\obs
docker compose -f compose-dev.yml down
```

Más consultas PromQL: `obs/prometheus/QUERIES-DEV.md`
