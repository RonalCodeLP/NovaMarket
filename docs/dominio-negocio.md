# Dominio de negocio — Plataforma POS multi-punto

NovaMarket modela la operación de **supermercados y cadenas comerciales a gran escala**: ventas en múltiples cajas, inventario, catálogo centralizado, pagos y control de usuarios por rol.

No representa un pequeño establecimiento aislado; representa la **capa operativa** que una organización necesita cuando crece más allá de un único punto de venta.

---

## Contexto de negocio

| Actor / escenario | Necesidad |
|-------------------|-----------|
| **Cadena de tiendas** | Mismo catálogo, ventas por sede, visibilidad central |
| **Varias cajas por local** | Cobros en paralelo sin caídas en hora pico |
| **Supervisores** | Historial, stock, catálogo — sin operar caja |
| **Administradores** | Configuración, roles, monitoreo |
| **Alto volumen** | Ventas y pagos desacoplados (sync + Kafka) |

---

## Entidades principales

| Concepto | Microservicio | API / tabla |
|----------|---------------|-------------|
| **Rubro** (categoría) | ms-rubro | `/api/v1/rubros`, `categorias` |
| **Artículo** (producto) | ms-articulo | `/api/v1/articulos`, `articulos` |
| **Venta** (orden POS) | ms-venta | `/api/v1/ventas`, `ordenes` + detalle |
| **Pago** | ms-pago | `/api/v1/pagos/registrar`, `pagos` |

---

## Pantallas Angular

| Ruta | Función |
|------|---------|
| `/auth` | Login Keycloak (usuarios de toda la cadena) |
| `/pos` | Caja — carrito, pago, boleta (cada terminal) |
| `/rubros` | CRUD categorías |
| `/articulos` | CRUD productos |
| `/existencias` | Alertas stock bajo |
| `/ventas` | Historial y reimpresión boleta |

---

## Flujo de venta (una caja de N)

1. Cajero escanea código de barras o busca artículo.  
2. Agrega al carrito.  
3. Elige medio: efectivo, tarjeta, Yape.  
4. Confirmar → `POST /api/v1/ventas` vía Gateway.  
5. ms-venta: persiste venta, descuenta stock (`ms-articulo`), registra pago (`ms-pago`), publica Kafka.  
6. UI muestra **número de boleta** (ej. `NM-00000001`).

En producción, **muchas cajas** ejecutan este flujo en paralelo; los microservicios escalan horizontalmente detrás del Gateway.

---

## Stock

- Campo `stock` en `articulos` (ms-articulo).  
- Descuento vía Feign al confirmar venta (consistencia entre venta e inventario).  
- Alertas: `GET /api/v1/articulos/alertas/stock-bajo`.

---

## Medios de pago

Registrados en ms-pago: **EFECTIVO**, **TARJETA**, **YAPE**.

---

## Datos de prueba (DEV)

El seeder carga rubros, artículos y ventas demo para simular una cadena en laboratorio. Login: **cajero**, **supervisor** o **admin** (ver [Seguridad](seguridad.md)).

---

## API vía Gateway (DEV :18080)

| Método | Ruta |
|--------|------|
| GET/POST | `/api/v1/rubros/**` |
| GET/POST/PUT/DELETE | `/api/v1/articulos/**` |
| POST/GET | `/api/v1/ventas` |
| POST | `/api/v1/pagos/registrar` |

Rutas protegidas requieren JWT Keycloak (ver [Seguridad](seguridad.md)).
