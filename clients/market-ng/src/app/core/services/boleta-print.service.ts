import { Injectable } from '@angular/core';
import { VentaResponse } from './ventas.service';
import { calcularIgv } from '../utils/igv.util';
import { crearPdfTermico } from '../utils/pdf-boleta.util';
import { etiquetaMedioPago, etiquetaTipoTarjeta } from '../utils/pago.util';

/** Ancho útil impresora Advance AW-711N y similares 58 mm ≈ 5,5 cm */
export const BOLETA_ANCHO_MM = 55;
const ANCHO_CARACTERES = 32;

@Injectable({ providedIn: 'root' })
export class BoletaPrintService {
  private iframeImpresion: HTMLIFrameElement | null = null;

  /**
   * Descarga PDF de la boleta y abre diálogo de impresión (térmica 55 mm).
   */
  emitirBoleta(venta: VentaResponse, opciones: { guardar?: boolean; imprimir?: boolean } = {}): void {
    const guardar = opciones.guardar !== false;
    const imprimir = opciones.imprimir !== false;

    if (guardar) {
      this.descargarPdf(venta);
    }
    if (imprimir) {
      setTimeout(() => this.imprimir(venta), guardar ? 150 : 50);
    }
  }

  descargarPdf(venta: VentaResponse): void {
    const base = this.nombreArchivo(venta);
    const bytes = crearPdfTermico(this.generarTexto(venta), BOLETA_ANCHO_MM);
    this.descargarBlob(`${base}.pdf`, bytes, 'application/pdf');
  }

  guardarArchivos(venta: VentaResponse): void {
    this.descargarPdf(venta);
  }

  imprimir(venta: VentaResponse): void {
    const html = this.generarHtml(venta, true);
    this.imprimirHtml(html);
  }

  /** Ticket de prueba 55 mm — use antes de la primera venta del día. */
  probarImpresora(): void {
    const demo: VentaResponse = {
      id: 0,
      cajeroUsername: 'PRUEBA',
      subtotal: 1.0,
      descuento: 0,
      total: 1.0,
      estado: 'DEMO',
      medioPago: 'EFECTIVO',
      montoRecibido: 1.0,
      vuelto: 0,
      numeroBoleta: 'NM-PRUEBA-00',
      fechaVenta: new Date().toISOString(),
      items: [
        {
          id: 0,
          productoId: 0,
          productoNombre: 'Prueba impresora AW-711N',
          cantidad: 1,
          precioUnitario: 1.0,
          subtotal: 1.0,
        },
      ],
    };

    const html = `<!DOCTYPE html>
<html lang="es"><head><meta charset="utf-8"/>
<style>
  @page { size: ${BOLETA_ANCHO_MM}mm auto; margin: 2mm; }
  body { width:${BOLETA_ANCHO_MM}mm;font-family:'Courier New',monospace;font-size:9pt;padding:2mm;text-align:center; }
  .ok { font-size:11pt;font-weight:bold;margin:2mm 0; }
</style></head><body>
  <div class="ok">NovaMarket</div>
  <div>PRUEBA DE IMPRESORA</div>
  <div>Advance AW-711N</div>
  <div>Ancho: ${BOLETA_ANCHO_MM} mm (5,5 cm)</div>
  <hr/>
  <div>${new Date().toLocaleString('es-PE')}</div>
  <div>Si ve este ticket, la térmica responde.</div>
  <div style="height:10mm"></div>
</body></html>`;

    this.imprimirHtml(html);
    this.descargarPdf(demo);
    localStorage.setItem('novamarket-impresora-probada', new Date().toISOString());
  }

  impresoraProbadaRecientemente(): boolean {
    const raw = localStorage.getItem('novamarket-impresora-probada');
    if (!raw) {
      return false;
    }
    const fecha = new Date(raw).getTime();
    return Date.now() - fecha < 24 * 60 * 60 * 1000;
  }

  fechaUltimaPruebaImpresora(): string | null {
    return localStorage.getItem('novamarket-impresora-probada');
  }

  generarTexto(venta: VentaResponse): string {
    const igv = calcularIgv(venta.total);
    const lineas: string[] = [];
    const sep = '-'.repeat(ANCHO_CARACTERES);

    const centro = (t: string) => this.centrar(t, ANCHO_CARACTERES);
    const par = (izq: string, der: string) => this.fila(izq, der, ANCHO_CARACTERES);

    lineas.push(centro('NovaMarket'));
    lineas.push(centro('RUC 20123456789'));
    lineas.push(centro('BOLETA DE VENTA'));
    lineas.push(centro(venta.numeroBoleta ?? `Venta #${venta.id}`));
    lineas.push(centro(this.formatearFecha(venta.fechaVenta)));
    lineas.push(sep);
    lineas.push(par('Cajero', venta.cajeroUsername));
    lineas.push(sep);

    for (const item of venta.items ?? []) {
      const nombre = this.truncar(item.productoNombre, ANCHO_CARACTERES);
      lineas.push(nombre);
      const det = ` ${item.cantidad} x S/${this.num(item.precioUnitario)}`;
      lineas.push(par(det, `S/${this.num(item.subtotal)}`));
    }

    lineas.push(sep);
    lineas.push(par('Subtotal', `S/${this.num(venta.subtotal)}`));
    if (venta.descuento > 0) {
      lineas.push(par('Descuento', `-S/${this.num(venta.descuento)}`));
    }
    lineas.push(par('Op. gravada', `S/${this.num(igv.opGravada)}`));
    lineas.push(par('IGV 18%', `S/${this.num(igv.igv)}`));
    lineas.push(par('TOTAL', `S/${this.num(venta.total)}`));
    lineas.push(sep);
    lineas.push(par('Pago', etiquetaMedioPago(venta.medioPago)));

    if (venta.tipoTarjeta) {
      lineas.push(par('Tarjeta', etiquetaTipoTarjeta(venta.tipoTarjeta)));
    }
    if (venta.codigoAutorizacion && venta.medioPago === 'TARJETA') {
      lineas.push(par('Autoriz.', venta.codigoAutorizacion));
    }
    if (venta.codigoOperacion && venta.medioPago === 'YAPE') {
      lineas.push(par('Op. Yape', venta.codigoOperacion));
    }
    if (venta.referenciaTransaccion) {
      lineas.push(this.partirReferencia(venta.referenciaTransaccion));
    }
    if (venta.medioPago === 'EFECTIVO' && venta.montoRecibido != null) {
      lineas.push(par('Recibido', `S/${this.num(venta.montoRecibido)}`));
    }
    if (venta.medioPago === 'EFECTIVO' && venta.vuelto != null) {
      lineas.push(par('Vuelto', `S/${this.num(venta.vuelto)}`));
    }

    lineas.push(sep);
    lineas.push(centro('Precios incluyen IGV'));
    lineas.push(centro('Gracias por su compra'));
    lineas.push('');
    lineas.push('');

    return lineas.join('\n');
  }

  generarHtml(venta: VentaResponse, soloImpresion = false): string {
    const igv = calcularIgv(venta.total);
    const items = (venta.items ?? [])
      .map(
        i => `
      <div class="item">
        <div class="item-nombre">${this.esc(i.productoNombre)}</div>
        <div class="item-linea">
          <span>${i.cantidad} x S/ ${this.num(i.precioUnitario)}</span>
          <span>S/ ${this.num(i.subtotal)}</span>
        </div>
      </div>`,
      )
      .join('');

    const pagoExtras: string[] = [];
    if (venta.tipoTarjeta) {
      pagoExtras.push(this.filaHtml('Tarjeta', etiquetaTipoTarjeta(venta.tipoTarjeta)));
    }
    if (venta.codigoAutorizacion && venta.medioPago === 'TARJETA') {
      pagoExtras.push(this.filaHtml('Autoriz.', venta.codigoAutorizacion));
    }
    if (venta.codigoOperacion && venta.medioPago === 'YAPE') {
      pagoExtras.push(this.filaHtml('Op. Yape', venta.codigoOperacion));
    }
    if (venta.referenciaTransaccion) {
      pagoExtras.push(`<div class="ref">${this.esc(venta.referenciaTransaccion)}</div>`);
    }
    if (venta.medioPago === 'EFECTIVO' && venta.montoRecibido != null) {
      pagoExtras.push(this.filaHtml('Recibido', `S/ ${this.num(venta.montoRecibido)}`));
    }
    if (venta.medioPago === 'EFECTIVO' && venta.vuelto != null) {
      pagoExtras.push(this.filaHtml('Vuelto', `S/ ${this.num(venta.vuelto)}`));
    }

    const descuentoHtml =
      venta.descuento > 0
        ? this.filaHtml('Descuento', `- S/ ${this.num(venta.descuento)}`)
        : '';

    return `<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="utf-8" />
  <title>${this.esc(venta.numeroBoleta ?? 'Boleta')}</title>
  <style>
    @page { size: ${BOLETA_ANCHO_MM}mm auto; margin: 2mm; }
    * { box-sizing: border-box; margin: 0; padding: 0; }
    body {
      width: ${BOLETA_ANCHO_MM}mm;
      max-width: ${BOLETA_ANCHO_MM}mm;
      font-family: 'Courier New', Courier, monospace;
      font-size: 9pt;
      line-height: 1.35;
      color: #000;
      background: #fff;
      padding: 2mm;
    }
    .c { text-align: center; }
    .sep { border-top: 1px dashed #000; margin: 3mm 0; }
    .titulo { font-size: 11pt; font-weight: bold; }
    .fila { display: flex; justify-content: space-between; gap: 2mm; margin: 1mm 0; }
    .total { font-weight: bold; font-size: 10pt; border-top: 1px solid #000; padding-top: 2mm; margin-top: 2mm; }
    .item { margin: 2mm 0; }
    .item-nombre { font-weight: bold; word-break: break-word; }
    .item-linea { display: flex; justify-content: space-between; font-size: 8pt; }
    .ref { font-size: 7pt; word-break: break-all; margin: 1mm 0; }
    .pie { text-align: center; font-size: 7pt; margin-top: 3mm; }
    ${soloImpresion ? '@media screen { body { display: none; } }' : ''}
  </style>
</head>
<body>
  <div class="c titulo">NovaMarket</div>
  <div class="c">RUC 20123456789</div>
  <div class="c"><strong>BOLETA DE VENTA</strong></div>
  <div class="c">${this.esc(venta.numeroBoleta ?? 'Venta #' + venta.id)}</div>
  <div class="c">${this.formatearFecha(venta.fechaVenta)}</div>
  <div class="sep"></div>
  <div class="fila"><span>Cajero</span><span>${this.esc(venta.cajeroUsername)}</span></div>
  <div class="sep"></div>
  ${items}
  <div class="sep"></div>
  <div class="fila"><span>Subtotal</span><span>S/ ${this.num(venta.subtotal)}</span></div>
  ${descuentoHtml}
  <div class="fila"><span>Op. gravada</span><span>S/ ${this.num(igv.opGravada)}</span></div>
  <div class="fila"><span>IGV 18%</span><span>S/ ${this.num(igv.igv)}</span></div>
  <div class="fila total"><span>TOTAL</span><span>S/ ${this.num(venta.total)}</span></div>
  <div class="fila"><span>Pago</span><span>${etiquetaMedioPago(venta.medioPago)}</span></div>
  ${pagoExtras.join('')}
  <div class="sep"></div>
  <div class="pie">Precios incluyen IGV<br/>¡Gracias por su compra!</div>
  <div style="height:8mm"></div>
</body>
</html>`;
  }

  private imprimirHtml(html: string): void {
    if (this.iframeImpresion) {
      document.body.removeChild(this.iframeImpresion);
      this.iframeImpresion = null;
    }

    const iframe = document.createElement('iframe');
    iframe.setAttribute('title', 'Impresión boleta térmica');
    iframe.style.position = 'fixed';
    iframe.style.right = '0';
    iframe.style.bottom = '0';
    iframe.style.width = '0';
    iframe.style.height = '0';
    iframe.style.border = 'none';
    document.body.appendChild(iframe);
    this.iframeImpresion = iframe;

    const doc = iframe.contentDocument ?? iframe.contentWindow?.document;
    if (!doc) {
      return;
    }

    doc.open();
    doc.write(html);
    doc.close();

    iframe.onload = () => {
      setTimeout(() => {
        iframe.contentWindow?.focus();
        iframe.contentWindow?.print();
        setTimeout(() => {
          if (this.iframeImpresion === iframe) {
            document.body.removeChild(iframe);
            this.iframeImpresion = null;
          }
        }, 1000);
      }, 200);
    };
  }

  private descargar(nombre: string, contenido: string, mime: string): void {
    const blob = new Blob([contenido], { type: mime });
    this.descargarBlob(nombre, blob, mime);
  }

  private descargarBlob(nombre: string, contenido: Blob | Uint8Array, mime: string): void {
    const part: BlobPart =
      contenido instanceof Blob ? contenido : new Uint8Array(contenido);
    const blob = contenido instanceof Blob ? contenido : new Blob([part], { type: mime });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = nombre;
    a.click();
    URL.revokeObjectURL(url);
  }

  private nombreArchivo(venta: VentaResponse): string {
    const id = (venta.numeroBoleta ?? `venta-${venta.id}`).replace(/[^\w-]/g, '_');
    return `boleta_${id}`;
  }

  private num(n: number | undefined): string {
    return (Number(n) || 0).toFixed(2);
  }

  private esc(s: string): string {
    return s
      .replace(/&/g, '&amp;')
      .replace(/</g, '&lt;')
      .replace(/>/g, '&gt;')
      .replace(/"/g, '&quot;');
  }

  private formatearFecha(fecha?: string): string {
    if (!fecha) {
      return new Date().toLocaleString('es-PE', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
      });
    }
    return new Date(fecha).toLocaleString('es-PE', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    });
  }

  private centrar(texto: string, ancho: number): string {
    const t = this.truncar(texto, ancho);
    const pad = Math.max(0, Math.floor((ancho - t.length) / 2));
    return ' '.repeat(pad) + t;
  }

  private fila(izq: string, der: string, ancho: number): string {
    const d = this.truncar(der, 12);
    const espacio = Math.max(1, ancho - izq.length - d.length);
    return this.truncar(izq, ancho - d.length - 1) + ' '.repeat(espacio) + d;
  }

  private filaHtml(izq: string, der: string): string {
    return `<div class="fila"><span>${this.esc(izq)}</span><span>${this.esc(der)}</span></div>`;
  }

  private truncar(texto: string, max: number): string {
    return texto.length <= max ? texto : texto.slice(0, max - 1) + '…';
  }

  private partirReferencia(ref: string): string {
    if (ref.length <= ANCHO_CARACTERES) {
      return ref;
    }
    const partes: string[] = [];
    for (let i = 0; i < ref.length; i += ANCHO_CARACTERES) {
      partes.push(ref.slice(i, i + ANCHO_CARACTERES));
    }
    return partes.join('\n');
  }
}
