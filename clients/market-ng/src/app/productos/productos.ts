import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Categoria, Producto, ProductoRequest, ProductosService } from './productos.service';
import { AuthService } from '../core/auth/auth.service';

@Component({
  selector: 'app-productos',
  imports: [CommonModule, FormsModule],
  templateUrl: './productos.html',
  styleUrl: './productos.scss',
})
export class Productos {
  productos = signal<Producto[]>([]);
  rubros = signal<Categoria[]>([]);
  loading = signal(false);
  error = signal('');
  formNombre = '';
  formDescripcion = '';
  formIdRubro: number|null = null;
  formPrecio: number|null = null;
  formStock = 0;
  formStockMinimo = 5;
  formCodigoBarras = '';
  editandoId: number|null = null;

  constructor(
    private productosService: ProductosService,
    protected auth: AuthService,
  ) {
    this.cargarRubros();
    this.cargarProductos();
  }

  cargarRubros() {
    this.productosService.listarRubros()
      .subscribe({
        next: rubros => this.rubros.set(rubros),
        error: () => this.error.set('No se pudieron cargar los rubros'),
      });
  }

  cargarProductos() {
    this.loading.set(true);
    this.error.set('');

    this.productosService.listar().subscribe({
      next: productos => this.productos.set(productos),
      error: () => {
        this.error.set('No se pudieron cargar los artículos');
        this.loading.set(false);
      },
      complete: () => this.loading.set(false),
    });
  }

  guardarProducto() {
    const producto = this.obtenerProductoDesdeFormulario();
    if (!producto) return;

    if (this.editandoId != null) {
      this.productosService.actualizar(this.editandoId, producto)
        .subscribe({
          next: () => {
            this.cancelarEdicion();
            this.cargarProductos();
          },
          error: () => this.error.set('No se pudo actualizar el artículo'),
        });
      return;
    }

    this.productosService.crear(producto)
      .subscribe({
        next: () => {
          this.limpiarFormulario();
          this.cargarProductos();
        },
        error: () => this.error.set('No se pudo crear el artículo'),
      });
  }

  iniciarEdicion(producto: Producto) {
    this.editandoId = producto.id;
    this.formNombre = producto.nombre;
    this.formDescripcion = producto.descripcion;
    this.formIdRubro = producto.idRubro;
    this.formPrecio = producto.precio ?? null;
    this.formStock = producto.stock ?? 0;
    this.formStockMinimo = producto.stockMinimo ?? 5;
    this.formCodigoBarras = producto.codigoBarras ?? '';
  }

  cancelarEdicion() {
    this.editandoId = null;
    this.limpiarFormulario();
  }

  eliminarProducto(id: number) {
    if (!confirm(`¿Está seguro de eliminar el artículo ${id}?`)) return;

    this.productosService.eliminar(id)
      .subscribe({
        next: () => this.cargarProductos(),
        error: () => this.error.set('No se pudo eliminar el artículo'),
      });
  }

  puedeGestionarProductos(): boolean {
    return this.auth.hasAnyRole(['ROLE_ADMIN', 'ADMIN']);
  }

  nombreRubro(idRubro: number): string {
    return this.rubros().find(rubro => rubro.id === idRubro)?.nombre ?? `${idRubro}`;
  }

  private obtenerProductoDesdeFormulario(): ProductoRequest|null {
    const nombre = this.formNombre.trim();
    const descripcion = this.formDescripcion.trim();
    const idRubro = Number(this.formIdRubro);

    const precio = Number(this.formPrecio);
    if (!nombre || !idRubro || !precio || precio <= 0) {
      this.error.set('Nombre, rubro y precio (> 0) son obligatorios');
      return null;
    }

    this.error.set('');
    return {
      nombre,
      descripcion,
      idRubro,
      precio,
      stock: this.formStock,
      stockMinimo: this.formStockMinimo,
      codigoBarras: this.formCodigoBarras.trim() || undefined,
    };
  }

  private limpiarFormulario() {
    this.formNombre = '';
    this.formDescripcion = '';
    this.formIdRubro = null;
    this.formPrecio = null;
    this.formStock = 0;
    this.formStockMinimo = 5;
    this.formCodigoBarras = '';
  }
}
