# Dominio de negocio — Minimarket POS

NovaMarket modela un **minimarket** con caja, inventario y catálogo.

---

## Entidades principales

| Concepto | Microservicio | API / tabla |
|----------|---------------|-------------|
| **Rubro** (categoría) | ms-rubro | `/api/v1/categorias`, `categorias` |
| **Artículo** (producto) | ms-articulo | `/api/v1/productos`, `productos` |
| **Cliente** | ms-cliente | `/api/v1/clientes` |
| **Venta** (orden POS) | ms-venta | `/api/v1/ventas`, `ordenes` + detalle |
| **Pago** | ms-pago | `/api/v1/pagos/registrar`, `pagos` |

---

## Pantallas Angular

| Ruta | Función |
|------|---------|
| `/auth` | Login Keycloak |
| `/pos` | Caja — carrito, pago, boleta |
| `/rubros` | CRUD categorías |
| `/articulos` | CRUD productos |
| `/existencias` | Alertas stock bajo |
| `/ventas` | Historial y reimpresión boleta |

---

## Flujo de venta

1. Escanear código de barras o buscar artículo.  
2. Agregar al carrito.  
3. Elegir medio: efectivo, tarjeta, Yape.  
4. Confirmar → `POST /api/v1/ventas`.  
5. ms-venta: persiste venta, descuenta stock, registra pago, publica Kafka.  
6. UI muestra **número de boleta** (ej. `NM-00000001`).

---

## Stock

- Campo `stock` en `productos` (ms-articulo).  
- Descuento vía Feign al confirmar venta.  
- Alertas: `GET /api/v1/productos/alertas/stock-bajo`.

---

## Medios de pago

Registrados en ms-pago: **EFECTIVO**, **TARJETA**, **YAPE** (según implementación POS).

---

## Datos de prueba

Antes de usar la caja:

1. Crear al menos un **rubro**.  
2. Crear un **artículo** con stock > 0 y código de barras.  
3. Login como **cajero** o **admin**.

---

## API vía Gateway (DEV :18080)

| Método | Ruta |
|--------|------|
| GET/POST | `/api/v1/categorias/**` |
| GET/POST/PUT/DELETE | `/api/v1/productos/**` |
| POST/GET | `/api/v1/ventas` |
| POST | `/api/v1/pagos/registrar` |
| GET/POST | `/api/v1/clientes/**` |

Rutas protegidas requieren JWT Keycloak (ver [Seguridad](seguridad.md)).
