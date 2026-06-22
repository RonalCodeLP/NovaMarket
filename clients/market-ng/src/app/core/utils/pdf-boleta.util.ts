/** Genera un PDF mínimo (texto Courier) para boleta térmica sin dependencias externas. */
export function crearPdfTermico(texto: string, anchoMm: number): Uint8Array {
  const lineas = texto.split('\n');
  const anchoPt = mmAPt(anchoMm);
  const altoLinea = 9;
  const margen = 8;
  const altoPt = Math.max(120, lineas.length * altoLinea + margen * 2 + 10);
  return construirPdf(anchoPt, altoPt, lineas, margen, altoLinea);
}

function construirPdf(
  anchoPt: number,
  altoPt: number,
  lineas: string[],
  margen: number,
  altoLinea: number,
): Uint8Array {
  const parts: string[] = ['%PDF-1.4\n'];
  const offsets: number[] = [0];

  const add = (s: string) => {
    offsets.push(parts.join('').length);
    parts.push(s);
  };

  add('1 0 obj<</Type/Catalog/Pages 2 0 R>>endobj\n');
  add('2 0 obj<</Type/Pages/Kids[3 0 R]/Count 1>>endobj\n');
  add(
    `3 0 obj<</Type/Page/MediaBox[0 0 ${fmt(anchoPt)} ${fmt(
      altoPt,
    )}]/Parent 2 0 R/Contents 4 0 R/Resources<</Font<</F1 5 0 R>>>>>>endobj\n`,
  );

  const streamLines: string[] = ['BT', '/F1 8 Tf'];
  let y = altoPt - margen;
  for (const linea of lineas) {
    streamLines.push(`1 0 0 1 ${margen} ${fmt(y)} Tm (${escaparPdf(linea)}) Tj`);
    y -= altoLinea;
  }
  streamLines.push('ET');
  const stream = streamLines.join('\n');
  add(`4 0 obj<</Length ${stream.length}>>stream\n${stream}\nendstream\nendobj\n`);
  add('5 0 obj<</Type/Font/Subtype/Type1/BaseFont/Courier>>endobj\n');

  const body = parts.join('');
  const xrefOffset = body.length;
  let xref = `xref\n0 ${offsets.length}\n0000000000 65535 f \n`;
  for (let i = 1; i < offsets.length; i++) {
    xref += `${String(offsets[i]).padStart(10, '0')} 00000 n \n`;
  }
  const trailer = `trailer<</Size ${offsets.length}/Root 1 0 R>>\nstartxref\n${xrefOffset}\n%%EOF`;
  const full = body + xref + trailer;
  return new TextEncoder().encode(full);
}

function mmAPt(mm: number): number {
  return (mm * 72) / 25.4;
}

function fmt(n: number): string {
  return n.toFixed(2).replace(/\.?0+$/, '') || '0';
}

function escaparPdf(texto: string): string {
  return texto
    .replace(/\\/g, '\\\\')
    .replace(/\(/g, '\\(')
    .replace(/\)/g, '\\)')
    .replace(/[^\x20-\x7E]/g, '?');
}
