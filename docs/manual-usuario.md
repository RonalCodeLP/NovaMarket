# Manual de usuario — NovaMarket

Guía detallada para **cajeros, supervisores y administradores** que usan la aplicación web **market-ng** (Angular).

**URL DEV:** http://localhost:4200  
**Acceso:** botón **Entrar con Keycloak** en la pantalla de login.

---

## 1. Perfiles y pantallas

### 1.1 Roles disponibles

| Rol | Usuario demo | Contraseña | Pantallas disponibles | Permisos especiales |
|-----|--------------|------------|------------------------|---------------------|
| **Administrador** | `admin` | `admin123` | Caja, Ventas, Existencias, Artículos, Rubros | CRUD completo en artículos/rubros |
| **Supervisor** | `supervisor` | `supervisor123` | Ventas, Existencias, Artículos, Rubros (sin Caja) | Crear/Editar artículos/rubros, no eliminar |
| **Cajero** | `cajero` | `cajero123` | Caja, Ventas, Existencias (solo lectura) | Solo ventas y consulta |

El menú lateral **solo muestra** lo que su rol permite. Si intenta acceder a una pantalla no autorizada, verá error 403.

### 1.2 Matriz de permisos

| Acción | Admin | Supervisor | Cajero |
|--------|-------|------------|--------|
| Vender en caja | ✅ | ❌ | ✅ |
| Ver historial ventas | ✅ | ✅ | ✅ |
| Reimprimir boleta | ✅ | ✅ | ✅ |
| Crear artículo | ✅ | ✅ | ❌ |
| Editar artículo | ✅ | ✅ | ❌ |
| Eliminar artículo | ✅ | ❌ | ❌ |
| Crear rubro | ✅ | ✅ | ❌ |
| Editar rubro | ✅ | ✅ | ❌ |
| Eliminar rubro | ✅ | ❌ | ❌ |
| Ajustar stock | ✅ | ✅ | ❌ |
| Ver existencias | ✅ | ✅ | ✅ |

---

## 2. Iniciar sesión

### 2.1 Flujo de login

1. Abra http://localhost:4200 en su navegador  
2. Si no hay sesión activa, será redirigido a **Auth**  
3. Clic en **Entrar con Keycloak**  
4. Será redirigido a la página de login de Keycloak  
5. Ingrese usuario y contraseña según su rol  
6. Será redirigido automáticamente a la pantalla principal según su rol:
   - **Cajero/Admin:** → **Caja** (`/pos`)
   - **Supervisor:** → **Ventas** (`/ventas`)

### 2.2 Cerrar sesión

Para cerrar sesión:
1. Clic en su nombre de usuario en la parte inferior del menú lateral
2. Clic en **Cerrar sesión**
3. Será redirigido a Keycloak y luego a la pantalla de login

!!! tip "Dos usuarios a la vez"
    Use **Chrome** para un usuario e **Incógnito o Edge** para otro. En dos pestañas del mismo Chrome, Keycloak comparte sesión y ambos quedan con el mismo usuario.

### 2.3 Recuperación de contraseña

En ambiente DEV, las contraseñas son fijas (`admin123`, `supervisor123`, `cajero123`). En producción, contacte al administrador del sistema.

---

## 3. Pantalla Caja (`/pos`) — Cajero

### 3.1 Descripción de la interfaz

La pantalla de Caja se divide en 3 secciones:

1. **Panel izquierdo:** Escaneo de código de barras y carrito
2. **Panel derecho:** Resumen de venta y botón de cobro
3. **Modal de checkout:** Selección de medio de pago y confirmación

### 3.2 Agregar productos

**Opción A: Escaneo con lector de código de barras**
1. Coloque el cursor en el campo **Código de barras**
2. Pase el producto por el lector
3. El código se ingresa automáticamente y el producto se agrega al carrito
4. El campo se enfoca automáticamente para el siguiente producto

**Opción B: Ingreso manual**
1. En **Código de barras**, escriba el código (ej. `7751010001234`)
2. Clic en **Agregar** o presione Enter
3. El artículo aparece en el **carrito** con precio, cantidad y subtotal

!!! info "Códigos de prueba"
    Use los códigos del seeder: `7751010001234` (Leche Gloria), `7751010001235` (Arroz Costeño), etc.

### 3.3 Modificar el carrito

**Cambiar cantidad:**
1. En la fila del producto, modifique el campo **Cantidad**
2. El subtotal se actualiza automáticamente
3. Si la cantidad excede el stock disponible, verá un error

**Eliminar un producto:**
1. Clic en el botón **×** al final de la fila
2. El producto se elimina del carrito

**Vaciar todo el carrito:**
1. Clic en el botón **Vaciar carrito** (icono de basura)
2. Todos los productos se eliminan

### 3.4 Aplicar descuento

1. En el campo **Descuento (S/)**, ingrese el monto a descontar
2. El total se actualiza automáticamente
3. El descuento es opcional y requiere autorización del supervisor en producción

### 3.5 Proceso de cobro

#### Paso 1: Seleccionar medio de pago

- **Efectivo:** Para pagos en efectivo
- **Tarjeta:** Para pagos con tarjeta de débito/crédito
- **Yape:** Para pagos con Yape (billetera digital)

#### Paso 2: Ingresar datos según medio de pago

**Efectivo:**
1. Ingrese el **Monto recibido** del cliente
2. El sistema calcula automáticamente el **Vuelto**
3. Ejemplo: Total S/35, Recibido S/50 → Vuelto S/15

**Tarjeta:**
1. Seleccione **Tipo de tarjeta:** Débito o Crédito
2. (En producción) Pase la tarjeta por el datáfono
3. Ingrese el **Código de autorización** del datáfono

**Yape:**
1. El cliente realiza el pago desde su celular
2. Ingrese el **Código de operación** de 6 dígitos del Yape
3. Ejemplo: `123456`

#### Paso 3: Confirmar pago

1. Clic en **Confirmar pago y boleta**
2. El sistema procesa la venta
3. Si es exitoso, se muestra la boleta
4. Si falla, verá un mensaje de error

!!! warning "Errores comunes en cobro"
    - **Stock insuficiente:** El producto no tiene suficiente stock
    - **Monto recibido insuficiente:** El efectivo es menor al total
    - **Código Yape inválido:** Debe ser 6 dígitos numéricos
    - **Servicio no disponible:** Verifique que el backend esté levantado

### 3.6 Boleta

Tras un cobro exitoso se muestra la **boleta** con:
- **Número de boleta:** Ej. `NM-00000001`
- **Fecha y hora** de la venta
- **Cajero** que realizó la venta
- **Detalle de items:** Cantidad, nombre, precio unitario, subtotal
- **Subtotal, IGV y total**
- **Medio de pago**
- **Vuelto** (si es efectivo)

**Acciones disponibles:**
- **Imprimir:** Abre diálogo de impresión para térmica 55mm
- **Guardar:** Descarga PDF de la boleta
- **Nueva venta:** Limpia la pantalla para el siguiente cliente

### 3.7 Flujo típico de venta

```text
Login → Caja → escanear producto 1 → escanear producto 2 → ... → seleccionar pago → 
ingresar datos → confirmar → boleta → imprimir → nueva venta
```

---

## 4. Pantalla Ventas (`/ventas`)

### 4.1 Listado de ventas

Muestra todas las ventas realizadas ordenadas por fecha descendente (más recientes primero).

**Columnas:**
- **Número:** Número de boleta
- **Fecha:** Fecha y hora de la venta
- **Cajero:** Usuario que realizó la venta
- **Total:** Monto total de la venta
- **Medio:** Medio de pago (Efectivo/Tarjeta/Yape)
- **Estado:** Estado de la venta (PAGADO)

### 4.2 Ver detalle de venta

1. Clic en el botón **Ver** en la fila de la venta
2. Se abre un modal con:
   - Información completa de la venta
   - Detalle de items
   - Datos de pago
   - Boleta en pantalla

### 4.3 Reimprimir boleta

1. Clic en el botón **Reimprimir** en la fila de la venta
2. Se abre el diálogo de impresión
3. Seleccione la impresora térmica y confirme

!!! info "Uso de reimprimir"
    Útil cuando el cliente pierde la boleta o la impresión falló durante la venta.

### 4.4 Filtros y búsqueda

- **Por fecha:** Seleccione rango de fechas
- **Por cajero:** Filtre por usuario específico
- **Por medio de pago:** Filtre por efectivo/tarjeta/Yape

---

## 5. Pantalla Rubros (`/rubros`) — Admin / Supervisor

### 5.1 Listar rubros

Muestra todas las categorías disponibles:
- **Nombre:** Ej. Lácteos, Abarrotes, Bebidas
- **Descripción:** Descripción opcional del rubro
- **Acciones:** Editar/Eliminar (según rol)

### 5.2 Crear rubro

1. Clic en el botón **Nuevo rubro**
2. Ingrese **Nombre** (obligatorio)
3. Ingrese **Descripción** (opcional)
4. Clic en **Guardar**

!!! warning "Rubros en uso"
    No puede eliminar rubros que tienen artículos asociados. Primero debe reasignar o eliminar los artículos.

### 5.3 Editar rubro

1. Clic en el botón **Editar** en la fila del rubro
2. Modifique los campos necesarios
3. Clic en **Guardar**

### 5.4 Eliminar rubro

1. Clic en el botón **Eliminar** en la fila del rubro (solo admin)
2. Confirme la eliminación

---

## 6. Pantalla Artículos (`/articulos`) — Admin / Supervisor

### 6.1 Listar artículos

Muestra todos los productos del catálogo:
- **Código:** Código de barras
- **Nombre:** Nombre del producto
- **Rubro:** Categoría
- **Precio:** Precio unitario
- **Stock:** Cantidad disponible
- **Stock mínimo:** Umbral para alertas
- **Acciones:** Editar/Eliminar (según rol)

### 6.2 Crear artículo

1. Clic en el botón **Nuevo artículo**
2. Complete los campos:
   - **Nombre** (obligatorio)
   - **Rubro** (obligatorio, seleccionar de lista)
   - **Precio** (obligatorio, mayor a 0)
   - **Stock** (obligatorio, no negativo)
   - **Stock mínimo** (obligatorio, para alertas)
   - **Código de barras** (obligatorio, único)
3. Clic en **Guardar**

!!! info "Código de barras"
    El código de barras es lo que el cajero escanea en Caja. Asegúrese de que sea único y coincida con el producto físico.

### 6.3 Editar artículo

1. Clic en el botón **Editar** en la fila del artículo
2. Modifique los campos necesarios
3. Clic en **Guardar**

### 6.4 Eliminar artículo

1. Clic en el botón **Eliminar** en la fila del artículo (solo admin)
2. Confirme la eliminación

!!! warning "Artículos con ventas"
    No se recomienda eliminar artículos que ya tienen ventas registradas. Mejor desactívelos poniendo stock en 0.

---

## 7. Pantalla Existencias (`/existencias`)

### 7.1 Alertas de stock bajo

Muestra artículos con stock por debajo del mínimo configurado:
- **Código:** Código de barras
- **Nombre:** Nombre del producto
- **Stock actual:** Cantidad disponible
- **Stock mínimo:** Umbral configurado
- **Diferencia:** Cuánto falta para llegar al mínimo

!!! tip "Gestión proactiva"
    Revise esta pantalla diariamente para reabastecer productos antes de que se agoten.

### 7.2 Ajuste manual de stock

1. Clic en el botón **Ajustar stock** en la fila del artículo
2. Ingrese el **nuevo stock**
3. Seleccione el **motivo** del ajuste (ej. merma, ingreso, conteo)
4. Clic en **Guardar**

!!! warning "Permisos"
    Cajeros solo pueden consultar. Solo admin y supervisor pueden ajustar stock.

### 7.3 Historial de movimientos

Muestra todos los movimientos de inventario:
- **Fecha:** Fecha y hora del movimiento
- **Artículo:** Producto afectado
- **Tipo:** Ingreso/Salida/Ajuste
- **Cantidad:** Cantidad movida
- **Motivo:** Razón del movimiento
- **Usuario:** Quién realizó el movimiento

---

## 8. Flujo típico del día (cajero)

```text
Inicio de turno:
1. Login como cajero
2. Verificar stock en Existencias
3. Revisar productos con alertas

Durante el día:
4. Atender clientes en Caja
5. Escanear productos
6. Cobrar y emitir boleta
7. Nueva venta para siguiente cliente

Fin de turno:
8. Consultar ventas del día en Ventas
9. Verificar total vendido
10. Cerrar sesión
```

---

## 9. Buenas prácticas

### 9.1 Para cajeros

- **Escaneo rápido:** Mantenga el cursor en el campo de código para escaneo continuo
- **Verificación de precios:** Confirme que el precio en pantalla coincida con el etiquetado
- **Manejo de errores:** Si falla el cobro, verifique stock y conexión antes de reintentar
- **Boletas:** Imprima siempre la boleta para el cliente

### 9.2 Para supervisores

- **Revisión diaria:** Consulte existencias cada mañana
- **Alertas de stock:** Reabaste antes de que se agoten productos clave
- **Validación de precios:** Verifique que los precios estén actualizados
- **Auditoría:** Revise ventas del día para detectar anomalías

### 9.3 Para administradores

- **Gestión de usuarios:** Cree y elimine usuarios según necesidad
- **Configuración de rubros:** Mantenga categorías organizadas
- **Control de stock:** Realice conteos físicos periódicos
- **Seguridad:** Cambie contraseñas periódicamente

---

## 10. Soporte y troubleshooting

| Problema | Causa probable | Solución |
|----------|---------------|----------|
| No carga la app | `ng serve` no está corriendo | Inicie `ng serve` en `clients/market-ng` |
| Error al login | Keycloak no está levantado | Verifique Keycloak en http://localhost:41880 |
| 401 en operaciones | Token expirado | Cierre sesión y vuelva a entrar |
| 403 en operaciones | Rol incorrecto | Use el usuario correcto para la operación |
| Artículo no encontrado | Código incorrecto | Verifique código en Artículos (admin) |
| Stock insuficiente | No hay suficiente producto | Ajuste stock o informe al supervisor |
| Cobro falla | Backend no disponible | Verifique gateway, ms-venta, ms-articulo, ms-pago |
| Boleta no imprime | Impresora no configurada | Configure impresora térmica 55mm |
| Multi-caja no funciona | Solo 1 instancia ms-venta | Levante segunda instancia |

---

## 11. Atajos de teclado

| Atajo | Función |
|-------|---------|
| Enter (en código) | Agregar producto al carrito |
| Tab | Navegar entre campos del checkout |
| Esc | Cerrar modal |
| F5 | Recargar página |

---

## 12. Documentación técnica

Para más detalles técnicos, consulte:
- [Manual de funcionamiento](manual-funcionamiento.md) — Arquitectura y flujos técnicos
- [Frontend funcionalidad](frontend-funcionalidad.md) — Detalle de componentes Angular
- [Desarrollo](desarrollo.md) — Guía de desarrollo local
- [Seguridad](seguridad.md) — Keycloak y roles
