# Contenido del curso — NovaMarket

Mapa de sesiones alineado al curso de **Sistemas Distribuidos**, adaptado al dominio **plataforma POS multi-punto** (supermercados y cadenas comerciales a gran escala).

---

## U1 — Sistema distribuido base orientado a producción

**Producto U1:** sistema distribuido base funcional, configurable y preparado para múltiples instancias.

**Resultado U1:** construir servicios REST funcionales, externalizar configuración, registrar servicios dinámicamente, acceder mediante Gateway y demostrar balanceo.

| Sesión | Tema | Producto de sesión en NovaMarket | Documentación |
|--------|------|----------------------------------|---------------|
| **S1** | Construcción de un servicio base | `ms-rubro` funcional: CRUD categorías, PostgreSQL, Swagger, Actuator, Flyway | [Desarrollo](../desarrollo.md) · [Dominio](../dominio-negocio.md) |
| **S2** | Configuración centralizada | Config Server + `config-repo` con perfiles `dev`/`prod` | [Arquitectura](../arquitectura.md) · [s00-infraestructura](s00-infraestructura.md) |
| **S3** | Registro y descubrimiento | Eureka operativo; MS registrados (`MS-RUBRO`, `MS-ARTICULO`, etc.) | [Desarrollo](../desarrollo.md) |
| **S4** | Gateway y balanceo | Gateway `:18080` enruta `/api/v1/**` a microservicios | [s04-gateway](s04-gateway.md) |
| **S5** | Evaluación U1 | Sistema base integrado funcionando como un todo | [Puertos](../puertos.md) |

---

## U2 — Sistema distribuido robusto

**Producto U2:** sistema seguro, resiliente, consistente, observable e integrado con frontend.

**Resultado U2:** comunicación resiliente, seguridad OIDC, Kafka, consistencia venta-pago, observabilidad y cliente Angular.

| Sesión | Tema | Producto de sesión en NovaMarket | Documentación |
|--------|------|----------------------------------|---------------|
| **S6** | Comunicación sincrona resiliente | Circuit Breaker en `ms-articulo` → `ms-rubro` (Feign + Resilience4j) | [Arquitectura](../arquitectura.md) |
| **S7** | Seguridad distribuida | Keycloak realm `novamarket`, roles, JWT en Gateway y `ms-articulo` | [Seguridad](../seguridad.md) · [s06-seguridad](s06-seguridad.md) |
| **S8** | Mensajería asíncrona | Tópico `orden-eventos`; `ms-venta` publica, `ms-pago` consume | [Kafka](../kafka-eventos.md) · [s07-kafka-ingesta](s07-kafka-ingesta.md) |
| **S9** | Consistencia distribuida | Venta: Feign sync a pago + stock; Kafka async idempotente | [Dominio](../dominio-negocio.md) |
| **S10** | Observabilidad | Actuator, Prometheus, Loki, dashboard Grafana NovaMarket | [Observabilidad](../observabilidad.md) · [s05-observabilidad](s05-observabilidad.md) |
| **S11** | Integración frontend | `market-ng`: login OIDC, POS, guards por rol | [Desarrollo](../desarrollo.md) · [Seguridad](../seguridad.md) |
| **S12** | Evaluación U2 | Flujo caja completo + evidencias técnicas | [Rúbrica](../rubrica-evaluacion.md) |

---

## U3 — Validación y consolidación

**Producto U3 / producto del curso:** NovaMarket end-to-end, documentado, estabilizado y defendido.

| Sesión | Tema | Producto de sesión |
|--------|------|--------------------|
| **S13** | Validación end-to-end | Venta POS → stock → pago → boleta → Kafka → Grafana |
| **S14** | Revisión y estabilización | Documentación MkDocs, puertos, Keycloak, seeders |
| **S15** | Defensa técnica | Sustentación grupal del producto |
| **S16** | Evaluación final | Demostración individual de competencias |

Ver [Producto del curso](../producto-curso.md).

---

## Microservicios NovaMarket

| Servicio | Eureka ID | Responsabilidad |
|----------|-----------|-----------------|
| **ms-rubro** | `ms-rubro` | Categorías / rubros |
| **ms-articulo** | `ms-articulo` | Artículos, stock, circuit breaker |
| **ms-venta** | `ms-venta` | Ventas POS, boletas, productor Kafka |
| **ms-pago** | `ms-pago` | Pagos, consumidor Kafka |

---

## Roles operativos (cadena / multi-tienda)

| Usuario | Contraseña | Rol | Acceso principal |
|---------|------------|-----|------------------|
| `admin` | `admin123` | Administrador | Todo |
| `supervisor` | `supervisor123` | Supervisor | Ventas, catálogo, stock — sin caja |
| `cajero` | `cajero123` | Cajero | Caja y consultas |
