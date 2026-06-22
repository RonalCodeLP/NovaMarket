/** Utilidades de cobro estilo POS retail (Perú, PEN). */

export type TipoTarjeta = 'DEBITO' | 'CREDITO';

export type PasoCheckout = 'cerrado' | 'resumen' | 'cobro' | 'terminal' | 'procesando';

const BILLETES = [10, 20, 50, 100, 200] as const;

export function redondear(n: number): number {
  return Math.round(n * 100) / 100;
}

export function calcularVuelto(recibido: number, total: number): number {
  return redondear(Math.max(0, recibido - total));
}

/** Billetes rápidos útiles para el total (exacto + siguientes denominaciones). */
export function billetesSugeridos(total: number): number[] {
  const t = redondear(total);
  const sugeridos = new Set<number>([t]);
  for (const billete of BILLETES) {
    if (billete >= t) {
      sugeridos.add(billete);
    }
  }
  const ceil10 = Math.ceil(t / 10) * 10;
  if (ceil10 >= t) {
    sugeridos.add(ceil10);
  }
  return Array.from(sugeridos).sort((a, b) => a - b).slice(0, 6);
}

export function validarCodigoYape(codigo: string): boolean {
  return /^\d{6}$/.test(codigo.replace(/\D/g, ''));
}

export function normalizarCodigoYape(codigo: string): string {
  return codigo.replace(/\D/g, '').slice(0, 6);
}

export function validarAutorizacionTarjeta(codigo: string): boolean {
  return /^\d{6}$/.test(codigo);
}

/** Simula respuesta del datáfono (demo académica). */
export function generarAutorizacionTarjeta(): string {
  return String(Math.floor(100000 + Math.random() * 900000));
}

export const MENSAJES_TERMINAL = [
  'Conectando con VisaNet...',
  'Inserte, acerque o deslice la tarjeta...',
  'Autorizando transacción...',
  'Cobro aprobado por el banco',
] as const;
