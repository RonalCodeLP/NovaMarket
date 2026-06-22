# NovaMarket

Plataforma de **minimarket / punto de venta (POS)** basada en **microservicios**: Spring Boot 3.5, Spring Cloud, PostgreSQL, Apache Kafka, observabilidad (Prometheus, Loki, Grafana) e identidad con **Keycloak** (OAuth2/OIDC).

---

## Documentación

| Documento | Contenido |
|-----------|-----------|
| [Arquitectura](docs/arquitectura.md) | Diagramas, componentes, flujos |
| [Desarrollo (DEV)](docs/desarrollo.md) | Arranque local paso a paso |
| [Producción (PROD)](docs/produccion.md) | Despliegue con Docker |
| [Seguridad](docs/seguridad.md) | Keycloak, JWT, roles |
| [Observabilidad](docs/observabilidad.md) | Actuator, Prometheus, Loki, Grafana |
| [Kafka y eventos](docs/kafka-eventos.md) | Tópicos, productores, consumidores |
| [Dominio de negocio](docs/dominio-negocio.md) | Rubros, artículos, ventas, pagos |
| [Referencia de puertos](docs/puertos.md) | DEV vs PROD |

Libro digital (MkDocs): carpeta [`docs/`](docs/) — `mkdocs serve` si tienes MkDocs instalado.

---

## Inicio rápido DEV

```powershell
docker network create market-dev-net

cd keycloak ; .\start-dev.ps1

# 3 terminales infra
cd infra\config-server   ; mvn spring-boot:run
cd infra\registry-server ; mvn spring-boot:run
cd infra\gateway         ; mvn spring-boot:run

# Postgres + microservicios (ver docs/desarrollo.md)
cd clients\market-ng ; npm install ; ng serve
```

- **Frontend:** http://localhost:4200  
- **Gateway:** http://localhost:18080  
- **Keycloak:** http://localhost:41880/admin  
- **Login demo:** `cajero` / `cajero123`

---

## Stack tecnológico

| Capa | Tecnología |
|------|------------|
| Backend | Java 17, Spring Boot 3.5, Spring Cloud |
| Datos | PostgreSQL 16 |
| Mensajería | Apache Kafka (KRaft) |
| Identidad | Keycloak 25 (OIDC) |
| Frontend | Angular 21 |
| Observabilidad | Prometheus, Loki, Promtail, Grafana |
| Contenedores | Docker Compose |

---

## Estructura del repositorio

```text
NovaMarket/
├── infra/           Config Server, Eureka, Gateway, config-repo
├── keycloak/        IdP OIDC (realm novamarket)
├── services/        Microservicios de negocio
├── clients/market-ng/   SPA Angular (POS)
├── kafka/           Broker + UI + exporter
├── obs/             Prometheus, Loki, Grafana
└── docs/            Documentación del proyecto
```

---

## Estado funcional

| Capacidad | DEV | PROD |
|-----------|-----|------|
| Login Keycloak + Angular | Sí | Sí (configurar URLs prod) |
| Gateway + Eureka + Config | Sí | Sí (Docker) |
| CRUD rubros / artículos | Sí | Sí |
| Caja (ventas + pago) | Sí | Sí |
| Circuit Breaker (articulo→rubro) | Sí | Sí |
| Kafka eventos venta/pago | Sí | Sí |
| Observabilidad centralizada | Sí | Sí |

---

## Licencia y uso

Proyecto educativo — UPEU / curso de microservicios.
