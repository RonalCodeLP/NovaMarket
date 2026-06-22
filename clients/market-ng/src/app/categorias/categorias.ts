import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Categoria, CategoriaRequest, CategoriasService } from './categorias.service';

@Component({
  selector: 'app-categorias',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './categorias.html',
  styleUrl: './categorias.scss',
})
export class Categorias {
  rubros = signal<Categoria[]>([]);
  loading = signal(false);
  error = signal('');
  formNombre = '';
  formDescripcion = '';
  editandoId: number | null = null;

  constructor(private categoriasService: CategoriasService) {
    this.cargarRubros();
  }

  cargarRubros() {
    this.loading.set(true);
    this.error.set('');

    this.categoriasService.listar()
      .subscribe({
        next: (rubros) => {
          this.rubros.set(rubros);
          this.loading.set(false);
        },
        error: (err) => {
          console.error('Error al cargar rubros:', err);
          this.error.set('No se pudieron cargar los rubros. ¿Gateway (:18080) y ms-rubro (:8081) están arriba?');
          this.loading.set(false);
        },
      });
  }

  guardarRubro() {
    const rubro = this.obtenerRubroDesdeFormulario();
    if (!rubro) return;

    if (this.editandoId != null) {
      this.categoriasService.actualizar(this.editandoId, rubro)
        .subscribe({
          next: () => {
            this.cancelarEdicion();
            this.cargarRubros();
          },
          error: () => this.error.set('No se pudo actualizar el rubro'),
        });
      return;
    }

    this.categoriasService.crear(rubro)
      .subscribe({
        next: () => {
          this.limpiarFormulario();
          this.cargarRubros();
        },
        error: () => this.error.set('No se pudo crear el rubro'),
      });
  }

  eliminarRubro(id: number) {
    if (!confirm(`¿Está seguro de eliminar el rubro ${id}?`)) return;

    this.categoriasService.eliminar(id)
      .subscribe({
        next: () => this.cargarRubros(),
        error: () => this.error.set('No se pudo eliminar el rubro'),
      });
  }

  iniciarEdicion(rubro: Categoria) {
    this.editandoId = rubro.id;
    this.formNombre = rubro.nombre;
    this.formDescripcion = rubro.descripcion;
  }

  cancelarEdicion() {
    this.editandoId = null;
    this.limpiarFormulario();
  }

  /** @deprecated usar cargarRubros */
  cargarCategorias() {
    this.cargarRubros();
  }

  /** @deprecated usar guardarRubro */
  guardarCategoria() {
    this.guardarRubro();
  }

  /** @deprecated usar eliminarRubro */
  eliminarCategoria(id: number) {
    this.eliminarRubro(id);
  }

  private obtenerRubroDesdeFormulario(): CategoriaRequest | null {
    const nombre = this.formNombre.trim();
    const descripcion = this.formDescripcion.trim();

    if (!nombre || !descripcion) {
      this.error.set('Nombre y descripción son obligatorios');
      return null;
    }

    this.error.set('');
    return { nombre, descripcion };
  }

  private limpiarFormulario() {
    this.formNombre = '';
    this.formDescripcion = '';
  }
}
