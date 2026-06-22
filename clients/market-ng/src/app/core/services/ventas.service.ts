import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, timeout, TimeoutError } from 'rxjs';
import { ApiService } from './api.service';

const VENTA_TIMEOUT_MS = 15_000;

export type MedioPago = 'EFECTIVO' | 'TARJETA' | 'YAPE';
export type TipoTarjeta = 'DEBITO' | 'CREDITO';

export interface VentaItemRequest {
  productoId: number;
  cantidad: number;
}

export interface CrearVentaRequest {
  cajeroUsername: string;
  descuento?: number;
  medioPago: MedioPago;
  montoRecibido?: number;
  tipoTarjeta?: TipoTarjeta;
  codigoAutorizacion?: string;
  codigoOperacion?: string;
  items: VentaItemRequest[];
}

export interface VentaDetalleResponse {
  id: number;
  productoId: number;
  productoNombre: string;
  cantidad: number;
  precioUnitario: number;
  subtotal: number;
}

export interface VentaResponse {
  id: number;
  cajeroUsername: string;
  subtotal: number;
  descuento: number;
  total: number;
  estado: string;
  medioPago?: MedioPago;
  montoRecibido?: number;
  vuelto?: number;
  numeroBoleta?: string;
  fechaVenta?: string;
  pagoId?: number;
  codigoAutorizacion?: string;
  referenciaTransaccion?: string;
  tipoTarjeta?: TipoTarjeta;
  codigoOperacion?: string;
  monedaPago?: string;
  items: VentaDetalleResponse[];
}

@Injectable({ providedIn: 'root' })
export class VentasService {
  constructor(
    private http: HttpClient,
    private api: ApiService,
  ) {}

  crear(venta: CrearVentaRequest): Observable<VentaResponse> {
    return this.http
      .post<VentaResponse>(this.api.buildUrl('/api/v1/ventas'), venta)
      .pipe(timeout(VENTA_TIMEOUT_MS));
  }

  listar(): Observable<VentaResponse[]> {
    return this.http.get<VentaResponse[]>(this.api.buildUrl('/api/v1/ventas'));
  }

  obtener(id: number): Observable<VentaResponse> {
    return this.http.get<VentaResponse>(this.api.buildUrl(`/api/v1/ventas/${id}`));
  }
}
