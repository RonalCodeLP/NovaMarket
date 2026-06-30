# Sustentación del equipo (3 personas)

Guion y reparto para defensa oral centrado en **funcionalidades del frontend**.

**Duración:** ~18–20 min · **Orden demo:** Bonnier → Edy → Yefri

---

## Reparto

| Integrante | Módulo | Pantallas Angular |
|------------|--------|-------------------|
| **Bonnier** | Catálogo e inventario | `/rubros`, `/articulos`, `/existencias` |
| **Edy** | Identidad y cobro | `/auth`, panel Cobro en `/pos` |
| **Yefri** | Operación de caja | `/pos`, boleta, `/ventas`, demo multi-caja |

---

## Orden en vivo

1. **Bonnier** — Crear rubro + artículo con stock y código de barras  
2. **Edy** — Admin vs cajero (2 navegadores); medios de pago  
3. **Yefri** — Venta completa, boleta, historial, 2 cajeros simultáneos + Eureka (2 instancias ms-venta)

---

## Frases clave

- **Bonnier:** “Preparo qué se vende y cuánto hay.”  
- **Edy:** “Controlo quién entra y cómo se cobra.”  
- **Yefri:** “Muestro la venta real y varias cajas a la vez.”

---

## Checklist día D

- [ ] Keycloak :41880  
- [ ] Gateway + Eureka + 4 microservicios  
- [ ] 2 instancias ms-venta (19051, 19052)  
- [ ] Artículo con barcode y stock ≥ 10  
- [ ] Chrome + Incógnito para 2 usuarios  

Detalle de diapositivas y guion extendido: ver documento interno del equipo o solicitar al líder del repo.
