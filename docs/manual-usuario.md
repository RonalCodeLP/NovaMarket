# Manual de usuario — NovaMarket

Guía para **cajeros, supervisores y administradores** que usan la aplicación web **market-ng** (Angular).

**URL DEV:** http://localhost:4200  
**Acceso:** botón **Entrar con Keycloak** en la pantalla de login.

---

## 1. Perfiles y pantallas

| Rol | Usuario demo | Contraseña | Pantallas disponibles |
|-----|--------------|------------|------------------------|
| **Administrador** | `admin` | `admin123` | Caja, Ventas, Existencias, Artículos, Rubros |
| **Supervisor** | `supervisor` | `supervisor123` | Ventas, Existencias, Artículos, Rubros (sin Caja) |
| **Cajero** | `cajero` | `cajero123` | Caja, Ventas, Existencias (solo lectura) |

El menú lateral **solo muestra** lo que su rol permite.

---

## 2. Iniciar sesión

1. Abra http://localhost:4200  
2. Si no hay sesión, irá a **Auth**  
3. Clic en **Entrar con Keycloak**  
4. Ingrese usuario y contraseña  
5. Será redirigido a **Caja** (cajero/admin) o a **Ventas** (supervisor)

Para **cerrar sesión:** botón **Cerrar sesión** en la parte inferior del menú.

!!! tip "Dos usuarios a la vez"
    Use **Chrome** para un usuario e **Incógnito o Edge** para otro. En dos pestañas del mismo Chrome, Keycloak comparte sesión y ambos quedan con el mismo usuario.

---

## 3. Pantalla Caja (`/pos`) — Cajero

### 3.1 Agregar productos

1. En **Código de barras**, escanee o escriba el código (ej. del seeder)  
2. Clic en **Agregar** o Enter  
3. El artículo aparece en el **carrito** con precio y cantidad

### 3.2 Modificar carrito

- Cambie **cantidad** en la fila del producto  
- Use **×** para quitar una línea  
- **Vaciar carrito** limpia todo

### 3.3 Cobro

1. **Descuento (S/):** opcional  
2. **Forma de pago:** Efectivo, Tarjeta o Yape  
3. **Efectivo:** ingrese **Monto recibido** ≥ total (se calcula el vuelto)  
4. Clic en **Confirmar pago y boleta**

### 3.4 Boleta

Tras un cobro exitoso se muestra la **boleta** con número (ej. `NM-00000001`), ítems y total.  
Use **Nueva venta** para atender al siguiente cliente.

!!! warning "Si falla el cobro"
    Verifique que el backend esté levantado (gateway, ms-venta, ms-articulo, ms-pago) y que haya **stock** suficiente.

---

## 4. Pantalla Ventas (`/ventas`)

- Listado de ventas realizadas  
- Detalle y **reimpresión de boleta**  
- Disponible para cajero, supervisor y admin

---

## 5. Pantalla Rubros (`/rubros`) — Admin / Supervisor

- **Listar** categorías (Lácteos, Abarrotes, etc.)  
- **Crear / editar / eliminar** (admin; supervisor según permisos)  
- Campos: nombre, descripción

Sin rubros no se pueden clasificar artículos nuevos.

---

## 6. Pantalla Artículos (`/articulos`) — Admin / Supervisor

- **Crear** producto: nombre, rubro, precio, stock, código de barras  
- **Editar / eliminar** (eliminar solo admin)  
- El **código de barras** es el que el cajero escanea en Caja

---

## 7. Pantalla Existencias (`/existencias`)

- Artículos con **stock bajo** respecto al mínimo  
- Cajero: solo consulta  
- Admin / supervisor: gestión de inventario según permisos

---

## 8. Flujo típico del día (cajero)

```text
Login → Caja → escanear productos → elegir pago → confirmar → boleta → Nueva venta
```

Consultar ventas del turno: menú **Ventas**.

---

## 9. Soporte

| Problema | Qué hacer |
|----------|-----------|
| No carga la app | Verificar `ng serve` y http://localhost:4200 |
| Error al login | Keycloak en http://localhost:41880 |
| 401 / 403 en operaciones | Cerrar sesión y volver a entrar; usar rol correcto |
| Artículo no encontrado | Verificar código en **Artículos** (admin) |

Documentación técnica: [Manual de funcionamiento](manual-funcionamiento.md) · [Desarrollo](desarrollo.md)
