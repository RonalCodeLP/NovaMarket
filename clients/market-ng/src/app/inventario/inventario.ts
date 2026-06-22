import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Producto, ProductosService } from '../productos/productos.service';
import { RoleAccessService } from '../core/auth/role-access.service';

@Component({
  selector: 'app-inventario',
  imports: [CommonModule],
  templateUrl: './inventario.html',
  styleUrl: './inventario.scss',
})
export class Inventario {
  protected readonly access = inject(RoleAccessService);
  alertas = signal<Producto[]>([]);
  loading = signal(false);
  error = signal('');

  constructor(private productosService: ProductosService) {
    this.cargar();
  }

  puedeEditarExistencias(): boolean {
    return this.access.permissions().canEditExistencias;
  }

  cargar() {
    this.loading.set(true);
    this.error.set('');
    this.productosService.alertasStockBajo().subscribe({
      next: productos => {
        this.alertas.set(productos);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('No se pudieron cargar las alertas');
        this.loading.set(false);
      },
    });
  }
}
