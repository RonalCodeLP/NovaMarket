export type TipoTarjeta = 'DEBITO' | 'CREDITO';

export function etiquetaTipoTarjeta(tipo: TipoTarjeta): string {
  return tipo === 'DEBITO' ? 'Débito' : 'Crédito';
}

export function etiquetaMedioPago(medio?: string): string {
  switch (medio) {
    case 'EFECTIVO':
      return 'Efectivo';
    case 'TARJETA':
      return 'Tarjeta débito/crédito';
    case 'YAPE':
      return 'Yape / Plin';
    default:
      return medio ?? '—';
  }
}
