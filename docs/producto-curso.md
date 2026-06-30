# Producto del curso

## Frase principal

**NovaMarket es una plataforma distribuida para la gestión de operaciones comerciales a gran escala, desarrollada con una arquitectura de microservicios. Diseñada para soportar múltiples puntos de venta, operaciones concurrentes y crecimiento horizontal, integra autenticación centralizada, administración de catálogo, inventario, ventas y pagos.**

## Qué NO es

NovaMarket **no** está pensado para un pequeño establecimiento aislado con una sola caja y un solo operador. Ese escenario no justifica microservicios: un monolito o un POS comercial cerrado sería suficiente.

## Qué SÍ es

Una **plataforma distribuida** para organizaciones que crecen y enfrentan:

| Presión real | Por qué importa |
|--------------|-----------------|
| **Varias cajas** en paralelo | Pico de ventas; cada caja debe responder sin bloquear al resto |
| **Varias tiendas / locales** | Catálogo e inventario centralizados o federados; operación 24/7 |
| **Muchos usuarios** (cajeros, supervisores, admins) | Seguridad, roles y sesiones centralizadas (Keycloak) |
| **Alto volumen de transacciones** | Ventas y pagos desacoplados; eventos Kafka para no saturar la caja |
| **Evolución independiente** | Pagos, inventario y ventas cambian a ritmos distintos |

Ese contexto **sí exige** arquitectura de microservicios: escalado horizontal, resiliencia, observabilidad y despliegue por componente.

## Producto del curso (U3)

> Sistema distribuido de microservicios end-to-end, configurable, escalable, seguro, resiliente, consistente, observable, integrado con frontend y defendido técnicamente.

## Resultado esperado del curso

Al finalizar, el estudiante implementa, integra y sustenta un sistema distribuido basado en microservicios. La solución debe ejecutarse de forma reproducible en **desarrollo** y **producción local**, exponer evidencias de:

- Configuración centralizada (Config Server) — mismos servicios, distintos ambientes y tiendas
- Registro y descubrimiento (Eureka) — **múltiples instancias** por microservicio
- Enrutamiento y punto único de acceso (Gateway) — todas las cajas y sedes entran por un borde
- Seguridad distribuida (Keycloak + JWT) — usuarios y roles en toda la red de tiendas
- Comunicación síncrona resiliente (OpenFeign + Circuit Breaker)
- Mensajería asíncrona (Kafka) — picos de ventas sin bloquear cobro
- Consistencia en ventas y pagos
- Observabilidad (Prometheus, Loki, Grafana) — salud de decenas de instancias

## Qué resuelve en el negocio

| Necesidad de la cadena | Componente NovaMarket |
|------------------------|------------------------|
| Cobrar en **múltiples cajas** | `market-ng` + `ms-venta` (instancias escalables) |
| Inventario compartido / por sede | `ms-articulo` |
| Catálogo unificado | `ms-rubro` + `ms-articulo` |
| Pagos desacoplados del flujo de caja | `ms-pago` + Kafka |
| Control de acceso por rol y sede | Keycloak (admin, supervisor, cajero) |
| Monitoreo operacional centralizado | Grafana + Prometheus |

## Por qué microservicios (defensa ante el docente)

1. **Escalabilidad:** Eureka + Gateway permiten levantar N instancias de `ms-venta` y `ms-pago` en horas pico.
2. **Disponibilidad:** Circuit Breaker en `ms-articulo` → `ms-rubro` evita que caiga todo el POS si falla el catálogo.
3. **Desacoplamiento:** Kafka separa la venta (respuesta inmediata al cajero) del procesamiento de pagos.
4. **Seguridad transversal:** Keycloak + Gateway validan identidad en todos los puntos de venta.
5. **Evolución:** Se puede actualizar `ms-pago` (nuevo medio de pago) sin redeployar ventas ni inventario.

## Clasificación del sistema

| Dimensión | NovaMarket |
|-----------|------------|
| Tipo principal | **Plataforma POS multi-punto** |
| Capa operativa | Ventas, inventario, pagos, identidad |
| Arquitectura | Microservicios + eventos + observabilidad |
| Usuarios | Cajero, supervisor, administrador de cadena |
| Escala objetivo | Varias cajas, varias tiendas, alto volumen transaccional |

## Stack tecnológico

| Capa | Tecnología |
|------|------------|
| Backend | Java 17, Spring Boot 3.5, Spring Cloud |
| Frontend | Angular 21 (SPA multi-caja) |
| Datos | PostgreSQL 16 (persistencia por dominio) |
| Mensajería | Apache Kafka |
| Identidad | Keycloak 25 (OIDC) |
| Observabilidad | Prometheus, Loki, Grafana |
| Contenedores | Docker Compose |

## Repositorio

```text
NovaMarket/
├── clients/market-ng/   SPA Angular (cajas, catálogo, ventas)
├── services/            ms-rubro, ms-articulo, ms-venta, ms-pago
├── infra/               Config Server, Eureka, Gateway, config-repo
├── keycloak/            Identidad OIDC (realm novamarket)
├── kafka/               Broker y Kafka UI
├── obs/                 Prometheus, Loki, Grafana
└── docs/                Esta documentación
```
