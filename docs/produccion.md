# Producción (PROD)

Modo **Docker Compose**: simula despliegue en servidor con red `market-prod-net` y perfiles `prod`.

---

## Requisitos

- Docker Desktop con suficiente RAM (recomendado ≥ 8 GB)
- Puertos libres según [Puertos](puertos.md) (columna PROD)

---

## 1. Red

```powershell
docker network create market-prod-net
```

---

## 2. Infraestructura

```powershell
cd C:\ms1\NovaMarket\infra
docker compose up -d --build
```

| Servicio | URL host |
|----------|----------|
| Config Server | http://localhost:28888 |
| Eureka | http://localhost:28761 |
| Gateway | http://localhost:28082/actuator/health |

El gateway valida JWT con:

```text
issuer-uri: http://keycloak:8080/realms/novamarket
```

(dentro de la red Docker; ver `infra/config-repo/gateway-prod.yml`)

---

## 3. Keycloak

```powershell
cd C:\ms1\NovaMarket\keycloak
copy .env.example .env
docker compose up -d --build
```

| URL | Uso |
|-----|-----|
| http://localhost:28180/admin | Consola admin |
| http://localhost:28180/realms/novamarket | Realm |

Keycloak debe estar en **`market-prod-net`** para que el gateway resuelva `http://keycloak:8080`.

---

## 4. Microservicios

Desde cada carpeta en `services/`:

```powershell
cd services\ms-rubro
docker compose up -d --build

cd ..\ms-articulo
docker compose up -d --build

cd ..\ms-venta
docker compose up -d --build

cd ..\ms-pago
docker compose up -d --build
```

Opcional escalar rubro:

```powershell
docker compose up -d --scale ms-rubro=3
```

Los microservicios **no exponen puertos al host** en PROD estricto; el acceso es vía **gateway :28082**.

---

## 5. Kafka y observabilidad (opcional)

```powershell
cd kafka ; docker compose up -d
cd ..\obs ; docker compose up -d
```

| Componente | URL PROD |
|------------|----------|
| Kafka UI | http://localhost:28085 |
| Grafana | http://localhost:23000 |
| Prometheus | http://localhost:29090 |

---

## 6. Frontend en PROD

Build de Angular con perfil producción:

```powershell
cd clients\market-ng
npm install
ng build --configuration production
```

`environment.prod.ts` apunta a:

- API: `http://localhost:28082`
- Keycloak: `http://localhost:28180`

Sirve la carpeta `dist/erpng/browser` con nginx u otro servidor estático. Configura redirect URIs en Keycloak para tu dominio real.

---

## Orden de arranque recomendado

1. Red `market-prod-net`  
2. Config Server (infra)  
3. Eureka + Gateway (infra)  
4. Keycloak  
5. PostgreSQL + microservicios  
6. Kafka / obs (opcional)  
7. Frontend estático  

---

## Diferencias clave vs DEV

| | DEV | PROD |
|---|-----|------|
| Microservicios | Maven, puertos fijos | Docker, vía gateway |
| Gateway | :18080 | :28082 |
| Keycloak | :41880 | :28180 |
| Config | `*-dev.yml` | `*-prod.yml` |
| Depuración | Logs en consola | `docker logs` |

---

## Apagar

```powershell
cd infra ; docker compose down
cd ..\keycloak ; docker compose down
# Repetir down en cada services/*/compose.yml usado
```
