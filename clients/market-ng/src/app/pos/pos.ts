import { Component, ElementRef, inject, OnDestroy, signal, viewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { TimeoutError } from 'rxjs';
import { AuthService } from '../core/auth/auth.service';
import { Boleta } from '../boleta/boleta';
import { MedioPago, TipoTarjeta, VentaResponse, VentasService } from '../core/services/ventas.service';
import { BoletaPrintService } from '../core/services/boleta-print.service';
import { calcularIgv } from '../core/utils/igv.util';
import { Producto, ProductosService } from '../productos/productos.service';
import { PosCartService } from './pos-cart.service';
import {
  PasoCheckout,
  billetesSugeridos,
  calcularVuelto,
  generarAutorizacionTarjeta,
  MENSAJES_TERMINAL,
  normalizarCodigoYape,
  validarCodigoYape,
} from './pos-payment.util';
import { etiquetaTipoTarjeta } from '../core/utils/pago.util';

@Component({
  selector: 'app-pos',
  imports: [CommonModule, FormsModule, Boleta, RouterLink],
  templateUrl: './pos.html',
  styleUrl: './pos.scss',
})
export class Pos implements OnDestroy {
  private readonly cart = inject(PosCartService);
  private readonly productosService = inject(ProductosService);
  private readonly ventasService = inject(VentasService);
  private readonly boletaPrint = inject(BoletaPrintService);
  protected readonly auth = inject(AuthService);

  readonly cartItems = this.cart.items;
  readonly subtotal = this.cart.subtotal;
  readonly itemCount = this.cart.itemCount;
  readonly mensajesTerminal = MENSAJES_TERMINAL;

  readonly mediosPago: { value: MedioPago; label: string; hint: string; icon: string }[] = [
    { value: 'EFECTIVO', label: 'Efectivo', hint: 'Soles en caja', icon: '💵' },
    { value: 'TARJETA', label: 'Tarjeta', hint: 'VisaNet / Izipay', icon: '💳' },
    { value: 'YAPE', label: 'Yape', hint: 'Código de operación', icon: '📱' },
  ];

  codigoBarras = '';
  descuento = 0;
  medioPago: MedioPago = 'EFECTIVO';
  montoRecibido: number | null = null;
  tipoTarjeta: TipoTarjeta = 'DEBITO';
  codigoYape = '';
  codigoAutorizacionTarjeta: string | null = null;

  ventaCompletada = signal<VentaResponse | null>(null);
  ultimoEscaneo = signal<string | null>(null);
  pasoCheckout = signal<PasoCheckout>('cerrado');
  indiceTerminal = signal(0);
  loading = signal(false);
  error = signal('');

  private readonly codigoInput = viewChild<ElementRef<HTMLInputElement>>('codigoInput');
  private terminalTimer: ReturnType<typeof setInterval> | null = null;

  total(): number {
    return Math.max(0, this.subtotal() - (Number(this.descuento) || 0));
  }

  desgloseIgv() {
    return calcularIgv(this.total());
  }

  billetesParaTotal(): number[] {
    return billetesSugeridos(this.total());
  }

  vuelto(): number {
    return calcularVuelto(Number(this.montoRecibido ?? 0), this.total());
  }

  puedeCobrar(): boolean {
    if (!this.cartItems().length) {
      return false;
    }
    switch (this.medioPago) {
      case 'EFECTIVO':
        return Number(this.montoRecibido ?? 0) >= this.total();
      case 'TARJETA':
        return true;
      case 'YAPE':
        return validarCodigoYape(this.codigoYape);
      default:
        return false;
    }
  }

  ngOnDestroy() {
    this.detenerTerminal();
  }

  buscarPorCodigo() {
    const codigo = this.codigoBarras.trim();
    if (!codigo) {
      return;
    }

    this.error.set('');
    this.loading.set(true);

    this.productosService.buscarPorCodigo(codigo).subscribe({
      next: producto => {
        try {
          this.agregarAlCarrito(producto);
          this.codigoBarras = '';
          this.ultimoEscaneo.set(producto.nombre);
          this.enfocarEscaneo();
        } catch (e) {
          this.error.set(e instanceof Error ? e.message : 'No se pudo agregar');
          this.enfocarEscaneo();
        }
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Artículo no encontrado para ese código');
        this.codigoBarras = '';
        this.enfocarEscaneo();
        this.loading.set(false);
      },
    });
  }

  private enfocarEscaneo() {
    setTimeout(() => this.codigoInput()?.nativeElement.focus(), 0);
  }

  agregarAlCarrito(producto: Producto) {
    this.cart.addProduct({
      id: producto.id,
      nombre: producto.nombre,
      precio: Number(producto.precio ?? 0),
      stock: Number(producto.stock ?? 0),
    });
  }

  cambiarCantidad(productoId: number, cantidad: number) {
    try {
      this.cart.updateCantidad(productoId, cantidad);
      this.error.set('');
    } catch (e) {
      this.error.set(e instanceof Error ? e.message : 'Cantidad inválida');
    }
  }

  quitar(productoId: number) {
    this.cart.remove(productoId);
  }

  limpiarCarrito() {
    this.cart.clear();
    this.descuento = 0;
    this.resetPago();
    this.ventaCompletada.set(null);
    this.cerrarCheckout();
  }

  onMedioPagoChange() {
    this.montoRecibido = null;
    this.codigoYape = '';
    this.codigoAutorizacionTarjeta = null;
    if (this.medioPago === 'TARJETA') {
      this.tipoTarjeta = 'DEBITO';
    }
  }

  usarMontoExacto() {
    this.montoRecibido = this.total();
  }

  usarBillete(monto: number) {
    this.montoRecibido = monto;
  }

  onCodigoYapeChange(valor: string) {
    this.codigoYape = normalizarCodigoYape(valor);
  }

  probarImpresora() {
    this.boletaPrint.probarImpresora();
    this.error.set('');
  }

  cancelarProcesando() {
    if (this.ventaCompletada()) {
      this.loading.set(false);
      this.error.set('');
      this.pasoCheckout.set('cerrado');
      return;
    }
    this.loading.set(false);
    this.pasoCheckout.set('resumen');
    this.error.set('Cobro cancelado.');
  }

  abrirCheckout() {
    if (!this.puedeCobrar()) {
      this.error.set(this.mensajeValidacionPago());
      return;
    }
    if (!this.auth.username()) {
      this.error.set('Debe iniciar sesión');
      return;
    }
    this.error.set('');
    this.codigoAutorizacionTarjeta = null;
    this.pasoCheckout.set(this.medioPago === 'TARJETA' ? 'cobro' : 'resumen');
  }

  cerrarCheckout() {
    if (this.loading()) {
      return;
    }
    this.detenerTerminal();
    this.pasoCheckout.set('cerrado');
    this.indiceTerminal.set(0);
  }

  volverAResumen() {
    this.detenerTerminal();
    this.codigoAutorizacionTarjeta = null;
    this.pasoCheckout.set('resumen');
    this.indiceTerminal.set(0);
  }

  iniciarCobroTarjeta() {
    this.error.set('');
    this.pasoCheckout.set('terminal');
    this.indiceTerminal.set(0);
    this.detenerTerminal();

    this.terminalTimer = setInterval(() => {
      const next = this.indiceTerminal() + 1;
      if (next >= MENSAJES_TERMINAL.length) {
        this.detenerTerminal();
        this.codigoAutorizacionTarjeta = generarAutorizacionTarjeta();
        this.ejecutarCobro();
        return;
      }
      this.indiceTerminal.set(next);
    }, 900);
  }

  confirmarCobro() {
    if (this.medioPago === 'TARJETA') {
      this.iniciarCobroTarjeta();
      return;
    }
    this.ejecutarCobro();
  }

  private ejecutarCobro() {
    if (!this.puedeCobrar()) {
      this.error.set(this.mensajeValidacionPago());
      this.pasoCheckout.set('resumen');
      return;
    }

    const username = this.auth.username();
    if (!username) {
      this.error.set('Debe iniciar sesión');
      return;
    }

    this.pasoCheckout.set('procesando');
    this.loading.set(true);
    this.error.set('');

    const payload = {
      cajeroUsername: username,
      descuento: this.descuento || 0,
      medioPago: this.medioPago,
      montoRecibido: this.medioPago === 'EFECTIVO' ? Number(this.montoRecibido) : undefined,
      tipoTarjeta: this.medioPago === 'TARJETA' ? this.tipoTarjeta : undefined,
      codigoAutorizacion: this.medioPago === 'TARJETA' ? this.codigoAutorizacionTarjeta ?? undefined : undefined,
      codigoOperacion: this.medioPago === 'YAPE' ? normalizarCodigoYape(this.codigoYape) : undefined,
      items: this.cartItems().map(line => ({
        productoId: line.productoId,
        cantidad: line.cantidad,
      })),
    };

    this.ventasService.crear(payload).subscribe({
      next: venta => {
        this.loading.set(false);
        this.error.set('');
        this.ventaCompletada.set(venta);
        this.cart.clear();
        this.resetPago();
        this.detenerTerminal();
        this.pasoCheckout.set('cerrado');
        this.indiceTerminal.set(0);
        setTimeout(() => this.boletaPrint.emitirBoleta(venta), 0);
      },
      error: (err: unknown) => {
        this.error.set(this.resolverErrorCobro(err));
        this.pasoCheckout.set(this.medioPago === 'TARJETA' ? 'cobro' : 'resumen');
        this.loading.set(false);
      },
    });
  }

  nuevaVenta() {
    this.ventaCompletada.set(null);
    this.ultimoEscaneo.set(null);
    this.resetPago();
    this.cerrarCheckout();
    this.error.set('');
    this.enfocarEscaneo();
  }

  lineTotal(line: { precio: number; cantidad: number }): number {
    return line.precio * line.cantidad;
  }

  etiquetaMedio(medio: MedioPago): string {
    return this.mediosPago.find(m => m.value === medio)?.label ?? medio;
  }

  etiquetaTarjeta(tipo: TipoTarjeta): string {
    return etiquetaTipoTarjeta(tipo);
  }

  private resetPago() {
    this.descuento = 0;
    this.montoRecibido = null;
    this.codigoYape = '';
    this.codigoAutorizacionTarjeta = null;
    this.tipoTarjeta = 'DEBITO';
  }

  private detenerTerminal() {
    if (this.terminalTimer) {
      clearInterval(this.terminalTimer);
      this.terminalTimer = null;
    }
  }

  private mensajeValidacionPago(): string {
    switch (this.medioPago) {
      case 'EFECTIVO':
        return `El monto recibido debe ser al menos S/ ${this.total().toFixed(2)}`;
      case 'YAPE':
        return 'Ingrese el código de operación Yape (7 dígitos del comprobante del cliente)';
      default:
        return 'Revise los datos de cobro';
    }
  }

  private resolverErrorCobro(err: unknown): string {
    if (err instanceof TimeoutError) {
      return 'El cobro tardó demasiado. Intente nuevamente.';
    }
    if (err instanceof Error) {
      if (err.name === 'TimeoutError' || err.message?.toLowerCase().includes('timeout')) {
        return 'El cobro tardó demasiado. Intente nuevamente.';
      }
      if (err.message?.trim()) {
        return err.message;
      }
    }
    if (err instanceof HttpErrorResponse) {
      return this.mensajeErrorCobro(err);
    }
    return 'No se pudo completar el cobro. Intente nuevamente.';
  }

  private mensajeErrorCobro(err: HttpErrorResponse): string {
    const body = err.error;
    if (body && typeof body === 'object' && 'message' in body && body.message) {
      return String(body.message);
    }
    if (typeof body === 'string' && body.trim()) {
      return body;
    }
    if (err.status === 0) {
      return 'Sin conexión con el servidor. Intente nuevamente.';
    }
    if (err.status === 401 || err.status === 403) {
      return 'Sesión inválida. Vuelva a iniciar sesión.';
    }
    if (err.status === 503 || err.status === 502 || err.status === 504) {
      return 'Servicio de pagos no disponible. Intente más tarde.';
    }
    if (err.status === 400) {
      return `Datos inválidos: ${this.extraerMensaje(err)}`;
    }
    if (err.status != null && err.status >= 500) {
      return 'Error del servidor. Intente nuevamente.';
    }
    if (err.status != null) {
      return `No se pudo completar el cobro (${err.status}).`;
    }
    return 'No se pudo completar el cobro. Revise la conexión con el servidor.';
  }

  private extraerMensaje(err: HttpErrorResponse): string {
    const body = err.error;
    if (body && typeof body === 'object' && 'message' in body && body.message) {
      return String(body.message);
    }
    if (typeof body === 'string' && body.trim()) {
      return body;
    }
    return 'revise stock, monto y sesión';
  }
}
