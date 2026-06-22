# Desarrollo (DEV)

Modo recomendado para programar, depurar y generar evidencias del curso.

## Requisitos

- **Java 17**, **Maven 3.9+**
- **Docker Desktop**
- **Node.js 20+**, npm
- **Git**

---

## 1. Red Docker (una sola vez)

```powershell
docker network create market-dev-net
docker network create market-prod-net
```

---

## 2. Keycloak (obligatorio para login)

```powershell
cd C:\ms1\NovaMarket\keycloak
.\start-dev.ps1
```

| URL | Uso |
|-----|-----|
| http://localhost:41880/admin | Consola (`admin` / `admin`) |
| http://localhost:41880/realms/novamarket | Realm |

> Primera ejecución: build Docker ~5–15 min.

---

## 3. Infraestructura (Docker — recomendado)

```powershell
cd C:\ms1\NovaMarket\infra
.\start-dev.ps1
```

Contenedores: `market-config-dev`, `market-eureka-dev`, `market-gateway-dev`

| Servicio | URL |
|----------|-----|
| Config Server | http://localhost:18888 |
| Eureka | http://localhost:18761 |
| Gateway | http://localhost:18080/actuator/health → `UP` |

Equivalente manual:

```powershell
docker network create market-dev-net
cd infra
docker compose -f compose-dev.yml up -d --build
```

### Alternativa: Maven (sin contenedores)

```powershell
cd infra\config-server   ; mvn spring-boot:run   # :18888
cd infra\registry-server ; mvn spring-boot:run   # :18761
cd infra\gateway         ; mvn spring-boot:run   # :18080
```

> Si la **infra está en Docker** y los **MS en Maven**, arranca cada MS con:
> `$env:EUREKA_INSTANCE_HOSTNAME="host.docker.internal"; mvn spring-boot:run`
> (así el gateway en Docker puede enrutar hacia el host).

---

## 4. PostgreSQL (Docker)

Por cada microservicio que vayas a usar:

```powershell
cd services\ms-rubro     ; docker compose -f compose-dev.yml up -d
cd services\ms-articulo  ; docker compose -f compose-dev.yml up -d
cd services\ms-venta     ; docker compose -f compose-dev.yml up -d
cd services\ms-pago      ; docker compose -f compose-dev.yml up -d
```

### Inventario y ventas precargados (Java — estilo Plaza Vea)

Al arrancar con Postgres **vacío**, cada microservicio ejecuta su **seeder Java**:

| Microservicio | Clase | Qué carga |
|---------------|-------|-----------|
| `ms-rubro` | `RubroDataSeeder` | **35 rubros** (`seed/RubroCatalog.java`) |
| `ms-articulo` | `ArticuloDataSeeder` | **420 artículos** (12 por rubro, precio/stock/EAN-13) |
| `ms-venta` | `VentaDataSeeder` | **25 ventas** de demostración (boletas NM-00000001…) |
| `ms-pago` | `PagoDataSeeder` | **25 pagos** vinculados a esas ventas |

Flyway solo crea **tablas** (`V1`, `V2` estructura). Los **datos** los inserta Java al hacer `mvn spring-boot:run`.

Para resetear inventario en otra PC:

```powershell
cd services\ms-rubro    ; docker compose -f compose-dev.yml down -v ; docker compose -f compose-dev.yml up -d
cd services\ms-articulo ; docker compose -f compose-dev.yml down -v ; docker compose -f compose-dev.yml up -d
cd services\ms-venta    ; docker compose -f compose-dev.yml down -v ; docker compose -f compose-dev.yml up -d
cd services\ms-pago     ; docker compose -f compose-dev.yml down -v ; docker compose -f compose-dev.yml up -d
cd services\ms-rubro    ; mvn spring-boot:run
cd services\ms-articulo ; mvn spring-boot:run
cd services\ms-venta    ; mvn spring-boot:run
cd services\ms-pago     ; mvn spring-boot:run
```

Ver en Angular: **Rubros**, **Artículos** y **Ventas**. API: http://localhost:18080/api/v1/rubros

### Códigos de barras para demo (420 artículos)

Para imprimir etiquetas y pegarlas en productos de demostración:

```powershell
cd C:\ms1\NovaMarket
.\scripts\export-codigos-barras.ps1
```

Genera:

| Archivo | Uso |
|---------|-----|
| `docs/codigos-barras-articulos.csv` | Excel / hoja de cálculo |
| `docs/codigos-barras-articulos.html` | Abrir en navegador → **Ctrl+P** (A4, 2 columnas) |

### Impresión térmica (Advance AW-711N — 55 mm)

Tras cada cobro, la Caja **descarga** `boleta_NM-*.txt` y `boleta_NM-*.html` y **abre el diálogo de impresión** a 55 mm (5,5 cm).

1. Conecte la **AW-711N** por USB o Bluetooth (driver Windows instalado).
2. En el diálogo de impresión, elija la térmica y marque **55 mm** / rollo continuo si aplica.
3. Para impresión silenciosa (sin confirmar cada vez): Chrome con `--kiosk-printing` o QZ Tray.

Reimprimir desde **Historial de ventas** → botón **Imprimir**.


---

## 5. Microservicios (1 terminal cada uno)

```powershell
# Infra en Docker → usar hostname para que el gateway enrute al host:
$env:EUREKA_INSTANCE_HOSTNAME="host.docker.internal"

cd services\ms-rubro     ; mvn spring-boot:run
cd services\ms-articulo  ; mvn spring-boot:run
cd services\ms-venta     ; mvn spring-boot:run
cd services\ms-pago      ; mvn spring-boot:run
```

Comprobar en Eureka: http://localhost:18761 — deben aparecer **MS-RUBRO**, **MS-ARTICULO**, **MS-VENTA**, **MS-PAGO**, etc.

---

## 6. Frontend Angular

```powershell
cd clients\market-ng
npm install
ng serve
```

http://localhost:4200 → **Entrar con Keycloak** → `cajero` / `cajero123`

Configuración: `src/environments/environment.ts`

---

## 7. Opcional: Kafka

```powershell
cd kafka
docker compose -f compose-dev.yml up -d
```

UI: http://localhost:41085

---

## 8. Opcional: Observabilidad

```powershell
cd obs
docker compose -f compose-dev.yml up -d
```

| Herramienta | URL |
|-------------|-----|
| Grafana | http://localhost:13000 (`admin`/`admin`) |
| Prometheus | http://localhost:19090 |

---

## Orden mínimo para probar la caja

1. Keycloak  
2. Config + Eureka + Gateway  
3. Postgres: rubro, articulo, venta, pago  
4. Maven: esos cuatro MS  
5. Angular  
6. Crear **1 rubro** y **1 artículo** con stock y código de barras  
7. Menú **Caja** → venta → confirmar pago  

---

## Solución de problemas

| Síntoma | Acción |
|---------|--------|
| 401 en APIs | ¿Keycloak arriba? ¿Login en Angular? |
| Gateway no encuentra MS | ¿Eureka muestra el servicio? ¿Config Server :18888? |
| Keycloak lento | Normal en primer `docker compose build` |
| Eureka vacío | Esperar ~30 s tras arrancar cada MS |

---

## Modo mixto (avanzado)

Infra en Docker PROD + MS en Maven:

```text
CONFIG_SERVER_URL=http://localhost:28888
eureka.client.service-url.defaultZone=http://localhost:28761/eureka
```

Ver [Producción](produccion.md).
