# ms-auth (deprecado)

Este microservicio fue **reemplazado por Keycloak** (`keycloak/` en la raíz del repo).

- **Identidad y login:** Keycloak realm `novamarket` (http://localhost:41880)
- **Frontend:** `clients/market-ng` usa OIDC (cliente `market-ng`)
- **Gateway / ms-articulo:** validan JWT con `issuer-uri` de Keycloak

No levantar `ms-auth` en el flujo DEV normal. Se conserva el código solo como referencia histórica del curso.
