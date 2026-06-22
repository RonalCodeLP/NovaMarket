import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationEnd, Router, RouterLink } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { filter } from 'rxjs';
import { VentaResponse, VentasService } from '../core/services/ventas.service';
import { BoletaPrintService } from '../core/services/boleta-print.service';
import { Boleta } from '../boleta/boleta';

@Component({
  selector: 'app-ventas-historial',
  imports: [CommonModule, RouterLink, Boleta],
  templateUrl: './ventas-historial.html',
  styleUrl: './ventas-historial.scss',
})
export class VentasHistorial implements OnInit {
  private readonly ventasService = inject(VentasService);
  private readonly boletaPrint = inject(BoletaPrintService);
  private readonly router = inject(Router);

  ventas = signal<VentaResponse[]>([]);
  seleccionada = signal<VentaResponse | null>(null);
  loading = signal(false);
  error = signal('');

  ngOnInit() {
    this.cargar();
    this.router.events
      .pipe(filter((e): e is NavigationEnd => e instanceof NavigationEnd))
      .subscribe(e => {
        if (e.urlAfterRedirects.startsWith('/ventas')) {
          this.cargar();
        }
      });
  }

  cargar() {
    this.loading.set(true);
    this.error.set('');
    this.ventasService.listar().subscribe({
      next: lista => {
        this.ventas.set(lista ?? []);
        this.loading.set(false);
      },
      error: (err: HttpErrorResponse) => {
        this.ventas.set([]);
        this.error.set(this.mensajeError(err));
        this.loading.set(false);
      },
    });
  }

  verBoleta(venta: VentaResponse) {
    this.seleccionada.set(venta);
  }

  cerrarBoleta() {
    this.seleccionada.set(null);
  }

  reimprimirBoleta(venta: VentaResponse) {
    this.boletaPrint.emitirBoleta(venta, { guardar: false, imprimir: true });
  }

  etiquetaMedio(medio?: string): string {
    switch (medio) {
      case 'EFECTIVO':
        return 'Efectivo';
      case 'TARJETA':
        return 'Tarjeta';
      case 'YAPE':
        return 'Yape';
      default:
        return medio ?? '—';
    }
  }

  private mensajeError(err: HttpErrorResponse): string {
    if (err.status === 0) {
      return 'Sin conexión. Verifique gateway (:18080) y ms-venta (:19051).';
    }
    return `No se pudo cargar el historial (${err.status}). ¿Está ms-venta activo?`;
  }
}
