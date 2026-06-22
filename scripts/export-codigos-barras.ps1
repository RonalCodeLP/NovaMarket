# Genera CSV + HTML imprimible con los 420 códigos EAN-13 del seeder ArticuloCatalog.
# Uso: .\scripts\export-codigos-barras.ps1

$ErrorActionPreference = 'Stop'
$root = Split-Path -Parent (Split-Path -Parent $MyInvocation.MyCommand.Path)
$catalogPath = Join-Path $root 'services\ms-articulo\src\main\java\com\upeu\producto\seed\ArticuloCatalog.java'
$docsPath = Join-Path $root 'docs'

function Get-Ean13 {
    param([int]$RubroId, [int]$Secuencia)
    $base12 = ('775{0:D2}{1:D7}' -f $RubroId, $Secuencia)
    $total = 0
    for ($i = 0; $i -lt 12; $i++) {
        $digito = [int][string]$base12[$i]
        $peso = if ($i % 2 -eq 0) { 1 } else { 3 }
        $total += $digito * $peso
    }
    $check = (10 - ($total % 10)) % 10
    return $base12 + [string]$check
}

$lines = Get-Content $catalogPath -Encoding UTF8
$rubroId = 0
$rubroName = ''
$idx = 0
$rows = New-Object System.Collections.Generic.List[object]

foreach ($line in $lines) {
    if ($line -match '^\s*//\s*(\d+)\s+(.+)$') {
        $rubroId = [int]$Matches[1]
        $rubroName = $Matches[2].Trim()
        $idx = 0
    }
    elseif ($line -match 'a\("(.+?)",') {
        $idx++
        $id = ($rubroId - 1) * 12 + $idx
        $nombre = $Matches[1]
        $codigo = Get-Ean13 -RubroId $rubroId -Secuencia $idx
        $rows.Add([pscustomobject]@{
                Id           = $id
                RubroId      = $rubroId
                Rubro        = $rubroName
                Articulo     = $nombre
                CodigoBarras = $codigo
            })
    }
}

if ($rows.Count -ne 420) {
    Write-Warning "Se esperaban 420 artículos; se generaron $($rows.Count)."
}

$csvPath = Join-Path $docsPath 'codigos-barras-articulos.csv'
$rows | Export-Csv -Path $csvPath -NoTypeInformation -Encoding UTF8

$htmlPath = Join-Path $docsPath 'codigos-barras-articulos.html'
$cards = ($rows | ForEach-Object {
    @"
    <div class="card">
      <div class="codigo">$($_.CodigoBarras)</div>
      <div class="nombre">$($_.Articulo)</div>
      <div class="meta">ID $($_.Id) · Rubro $($_.RubroId) · $($_.Rubro)</div>
    </div>
"@
}) -join "`n"

@"

<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="utf-8" />
  <title>NovaMarket — Códigos de barras demo (420)</title>
  <style>
    @page { size: A4; margin: 12mm; }
    * { box-sizing: border-box; }
    body { font-family: Arial, sans-serif; margin: 0; padding: 12mm; color: #111; }
    h1 { font-size: 16px; margin: 0 0 4px; }
    p.sub { font-size: 11px; color: #555; margin: 0 0 10px; }
    .grid { display: grid; grid-template-columns: 1fr 1fr; gap: 8mm; }
    .card {
      border: 1px dashed #999;
      border-radius: 6px;
      padding: 8mm 6mm;
      min-height: 42mm;
      page-break-inside: avoid;
    }
    .codigo {
      font-family: 'Courier New', monospace;
      font-size: 20px;
      font-weight: bold;
      letter-spacing: 1px;
      margin-bottom: 6px;
    }
    .nombre { font-size: 12px; font-weight: 600; line-height: 1.3; }
    .meta { font-size: 9px; color: #666; margin-top: 6px; }
    @media print {
      body { padding: 0; }
      .no-print { display: none; }
    }
  </style>
</head>
<body>
  <div class="no-print">
    <h1>NovaMarket — Etiquetas demo (420 artículos)</h1>
    <p class="sub">Imprima en A4 (2 columnas). Pegue cada etiqueta en el producto de demostración. EAN-13 prefijo 775 (Perú).</p>
    <p class="sub"><strong>Ctrl+P</strong> para imprimir · CSV: codigos-barras-articulos.csv</p>
    <hr />
  </div>
  <div class="grid">
$cards
  </div>
</body>
</html>
"@ | Set-Content -Path $htmlPath -Encoding UTF8

Write-Host "Generados $($rows.Count) codigos:"
Write-Host "  CSV:  $csvPath"
Write-Host "  HTML: $htmlPath"
