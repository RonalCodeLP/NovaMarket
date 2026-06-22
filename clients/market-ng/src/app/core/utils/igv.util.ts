/** Precios de retail en Perú incluyen IGV 18 %. */
export interface DesgloseIgv {
  total: number;
  opGravada: number;
  igv: number;
}

export function calcularIgv(total: number): DesgloseIgv {
  const t = Math.max(0, Number(total) || 0);
  const opGravada = redondear(t / 1.18);
  const igv = redondear(t - opGravada);
  return { total: t, opGravada, igv };
}

function redondear(n: number): number {
  return Math.round(n * 100) / 100;
}
