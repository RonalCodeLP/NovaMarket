import { Component, computed, inject, input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TipoTarjeta, VentaResponse } from '../core/services/ventas.service';
import { BoletaPrintService, BOLETA_ANCHO_MM } from '../core/services/boleta-print.service';
import { calcularIgv } from '../core/utils/igv.util';
import { etiquetaMedioPago, etiquetaTipoTarjeta } from '../core/utils/pago.util';

@Component({
  selector: 'app-boleta',
  imports: [CommonModule],
  templateUrl: './boleta.html',
  styleUrl: './boleta.scss',
})
export class Boleta {
  private readonly printService = inject(BoletaPrintService);

  venta = input.required<VentaResponse>();
  readonly anchoMm = BOLETA_ANCHO_MM;

  readonly desglose = computed(() => calcularIgv(this.venta().total));

  etiquetaMedio(medio?: string): string {
    return etiquetaMedioPago(medio);
  }

  etiquetaTarjeta(tipo?: TipoTarjeta): string {
    return tipo ? etiquetaTipoTarjeta(tipo) : '—';
  }

  imprimir() {
    this.printService.imprimir(this.venta());
  }

  guardar() {
    this.printService.guardarArchivos(this.venta());
  }

  imprimirYGuardar() {
    this.printService.emitirBoleta(this.venta());
  }
}
