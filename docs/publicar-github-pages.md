# Publicar documentación en GitHub Pages (como el proyecto del docente)

NovaMarket usa **MkDocs Material** (igual estilo que `261dist/ecom`). El sitio queda en:

**https://ronalcodelp.github.io/NovaMarket/**

---

## Cómo funciona (resumen)

```text
docs/*.md  +  mkdocs.yml
        ↓
GitHub Actions (workflow docs.yml)
        ↓
rama gh-pages (sitio HTML)
        ↓
GitHub Pages → enlace en About
```

---

## Paso 1 — Subir el código a GitHub

```powershell
cd C:\ms1\NovaMarket
git add docs/ mkdocs.yml requirements-docs.txt .github/workflows/docs.yml
git commit -m "docs: manuales y sitio MkDocs"
git push origin main
```

---

## Paso 2 — Ejecutar el workflow (si no corre solo)

1. GitHub → repositorio **NovaMarket**  
2. Pestaña **Actions**  
3. Workflow **Publicar documentación**  
4. **Run workflow** → rama `main` → Run  

Espere el check **verde** (~2–3 min).

---

## Paso 3 — Activar GitHub Pages (una sola vez)

1. **Settings** → **Pages**  
2. **Source:** Deploy from a branch  
3. **Branch:** `gh-pages` · carpeta **`/ (root)`**  
4. **Save**  

En 1–5 minutos la URL estará activa.

---

## Paso 4 — Enlace en About (como el docente)

1. En la página principal del repo, clic en **⚙** junto a **About**  
2. **Website:** `https://ronalcodelp.github.io/NovaMarket/`  
3. **Description:** ej. *Plataforma POS retail — microservicios, Keycloak, Kafka*  
4. **Topics:** `java`, `spring-boot`, `angular`, `microservices`, `keycloak`, `kafka`, `eureka`  
5. **Save changes**  

El enlace aparecerá debajo de **About** como en `261dist/ecom`.

---

## Paso 5 — Probar localmente antes de subir (opcional)

```powershell
pip install -r requirements-docs.txt
mkdocs serve
```

Abrir http://127.0.0.1:8000

---

## Estructura del sitio (menú)

| Sección | Contenido |
|---------|-----------|
| **Manuales** | Manual de usuario, Manual de funcionamiento |
| **Guía del proyecto** | Arquitectura, DEV, seguridad, Kafka, observabilidad |
| **Sustentación** | Reparto equipo de 3 |
| **Contenido del curso** | Sesiones U1/U2/U3 |

---

## Problemas frecuentes

| Problema | Solución |
|----------|----------|
| 404 en la URL | Activar Pages con rama `gh-pages`; esperar 5 min |
| Workflow falla | Ver log en Actions; instalar deps en `requirements-docs.txt` |
| About sin link | Pegar URL manualmente en Settings del About |
| Repo privado | Pages gratis solo en repos públicos (o plan de pago) |

---

## Diferencia con “carpeta /docs sola”

El docente puede usar **solo** `/docs` con HTML estático. Nosotros usamos **MkDocs** + Action que genera `site/` y publica en **`gh-pages`**. El resultado en About es el mismo: un enlace público a documentación profesional con buscador y modo oscuro.
