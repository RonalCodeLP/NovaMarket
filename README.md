# NovaMarket

**NovaMarket** es una **plataforma distribuida para la gestión de operaciones comerciales a gran escala**, desarrollada con una arquitectura de microservicios. Diseñada para soportar múltiples puntos de venta, operaciones concurrentes y crecimiento horizontal, integra autenticación centralizada, administración de catálogo, inventario, ventas y pagos.

Arquitectura de **microservicios** (Spring Boot 3.5, Spring Cloud, PostgreSQL, Kafka, observabilidad, Keycloak) pensada para **escalar** cajas, tiendas, usuarios y transacciones en supermercados y cadenas comerciales.

**Documentación en línea:** [ronalcodelp.github.io/NovaMarket](https://ronalcodelp.github.io/NovaMarket/)

---

## Documentación

| Documento | Contenido |
|-----------|-----------|
| [**Sitio MkDocs**](https://ronalcodelp.github.io/NovaMarket/) | Documentación web (como el proyecto del docente) |
| [Manual de usuario](docs/manual-usuario.md) | Guía cajero / supervisor / admin |
| [Manual de funcionamiento](docs/manual-funcionamiento.md) | Arquitectura, flujos, operación |
| [Publicar en GitHub Pages](docs/publicar-github-pages.md) | Activar enlace en About |

Libro digital (MkDocs): carpeta [`docs/`](docs/) — ver abajo.

### Publicar documentación (GitHub Pages)

Workflow: [`.github/workflows/docs.yml`](.github/workflows/docs.yml) — construye MkDocs y publica en la rama **`gh-pages`**.

**Pasos (una sola vez):**

1. Push a `main` (o ejecutar **Actions → Publicar documentación → Run workflow**).
2. Esperar el check **verde**.
3. **Settings → Pages → Source:** *Deploy from a branch*
4. **Branch:** `gh-pages` · **Folder:** `/ (root)` → **Save**
5. **About → Website:** `https://ronalcodelp.github.io/NovaMarket/`

> Si antes falló con error **404 deployment**, era porque Pages no estaba activo o se usaba *GitHub Actions* sin habilitarlo. Este workflow usa la rama **`gh-pages`** (más simple).

**URL:** https://ronalcodelp.github.io/NovaMarket/

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
