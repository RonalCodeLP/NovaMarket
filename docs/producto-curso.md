# Producto del curso

## Definición

**NovaMarket** es un sistema **POS y gestión operativa para minimarkets**, construido como **plataforma de microservicios end-to-end**: configurable, escalable, segura, resiliente, observable e integrada con frontend Angular.

No compite con un ERP comercial cerrado. Demuestra cómo diseñar un **sistema distribuido real** aplicado a un dominio que cualquier persona entiende: una bodega o minimarket.

## Producto del curso (U3)

> Sistema distribuido de microservicios end-to-end, configurable, escalable, seguro, resiliente, consistente, observable, integrado con frontend y defendido técnicamente.

## Resultado esperado del curso

Al finalizar, el estudiante implementa, integra y sustenta un sistema distribuido basado en microservicios. La solución debe ejecutarse de forma reproducible en **desarrollo** y **producción local**, exponer evidencias de:

- Configuración centralizada (Config Server)
- Registro y descubrimiento (Eureka)
- Enrutamiento y punto único de acceso (Gateway)
- Seguridad distribuida (Keycloak + JWT)
- Comunicación síncrona resiliente (OpenFeign + Circuit Breaker)
- Mensajería asíncrona (Kafka)
- Consistencia en ventas y pagos
- Observabilidad (Prometheus, Loki, Grafana)
- Integración frontend (Angular POS)

El producto se presenta en equipo; cada estudiante evidencia y defiende su aporte individual.

## Qué resuelve en el negocio

| Necesidad del minimarket | Componente NovaMarket |
|--------------------------|------------------------|
| Cobrar en caja | `market-ng` → `/pos` + `ms-venta` |
| Controlar stock | `ms-articulo` |
| Organizar productos | `ms-rubro` + `ms-articulo` |
| Registrar pagos | `ms-pago` (efectivo, tarjeta, Yape) |
| Separar roles | Keycloak: admin, supervisor, cajero |
| Monitorear operación | Grafana + Prometheus |

## Clasificación del sistema

| Dimensión | NovaMarket |
|-----------|------------|
| Tipo principal | **POS / Punto de venta** |
| Capa secundaria | **Gestión operativa** (catálogo, stock, historial) |
| Arquitectura | Microservicios + eventos |
| Usuarios finales | Cajero, supervisor, administrador |

## Stack tecnológico

| Capa | Tecnología |
|------|------------|
| Backend | Java 17, Spring Boot 3.5, Spring Cloud |
| Frontend | Angular 21 |
| Datos | PostgreSQL 16 |
| Mensajería | Apache Kafka |
| Identidad | Keycloak 25 (OIDC) |
| Observabilidad | Prometheus, Loki, Grafana |
| Contenedores | Docker Compose |

## Repositorio

```text
NovaMarket/
├── clients/market-ng/   SPA Angular (caja, catálogo, ventas)
├── services/            ms-rubro, ms-articulo, ms-venta, ms-pago
├── infra/               Config Server, Eureka, Gateway, config-repo
├── keycloak/            Identidad OIDC (realm novamarket)
├── kafka/               Broker y Kafka UI
├── obs/                 Prometheus, Loki, Grafana
└── docs/                Esta documentación
```
