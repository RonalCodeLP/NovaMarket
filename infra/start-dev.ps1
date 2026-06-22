# Arranque infra DEV (Config + Eureka + Gateway) en Docker
# Uso: .\start-dev.ps1

$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot

Write-Host "==> Red Docker market-dev-net"
docker network create market-dev-net 2>$null

Write-Host "==> Compilar y levantar infra DEV (18888 / 18761 / 18080)..."
docker compose -f compose-dev.yml up -d --build
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }

Write-Host "==> Esperando gateway..."
$maxAttempts = 30
$ready = $false

for ($i = 1; $i -le $maxAttempts; $i++) {
    $health = curl.exe -s -m 8 http://localhost:18080/actuator/health 2>$null
    if ($health -match '"status"\s*:\s*"UP"') {
        $ready = $true
        break
    }

    $status = docker inspect -f "{{.State.Status}}" market-gateway-dev 2>$null
    if ($status -ne "running") {
        Write-Host "Contenedor gateway detenido. Logs:"
        docker logs --tail 40 market-gateway-dev
        exit 1
    }

    Write-Host "  intento $i/$maxAttempts..."
    Start-Sleep -Seconds 5
}

if (-not $ready) {
    Write-Host "Gateway aun no responde UP. Ultimos logs:"
    docker logs --tail 30 market-gateway-dev
    exit 1
}

Write-Host ""
Write-Host "Infra DEV lista."
Write-Host "  Config:  http://localhost:18888"
Write-Host "  Eureka:  http://localhost:18761"
Write-Host "  Gateway: http://localhost:18080/actuator/health"
Write-Host ""
Write-Host "Requisito: Keycloak DEV en http://localhost:41880 (cd ..\keycloak ; .\start-dev.ps1)"
Write-Host "Microservicios en Maven: registrar con hostname host.docker.internal (ver docs/desarrollo.md)"
