import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiService } from '../core/services/api.service';

export interface Articulo {
  id: number;
  nombre: string;
  descripcion: string;
  idRubro: number;
  precio?: number;
  stock?: number;
  stockMinimo?: number;
  codigoBarras?: string;
  imagenUrl?: string;
  stockBajo?: boolean;
}

export interface Rubro {
  id: number;
  nombre: string;
}

export interface ArticuloRequest {
  nombre: string;
  descripcion: string;
  idRubro: number;
  precio: number;
  stock?: number;
  stockMinimo?: number;
  codigoBarras?: string;
  imagenUrl?: string;
}

/** @deprecated usar Articulo */
export type Producto = Articulo;
/** @deprecated usar ArticuloRequest */
export type ProductoRequest = ArticuloRequest;
/** @deprecated usar Rubro */
export type Categoria = Rubro;

@Injectable({ providedIn: 'root' })
export class ProductosService {
  constructor(
    private http: HttpClient,
    private api: ApiService,
  ) {}

  listar(): Observable<Articulo[]> {
    return this.http.get<Articulo[]>(this.api.buildUrl('/api/v1/articulos'));
  }

  listarRubros(): Observable<Rubro[]> {
    return this.http.get<Rubro[]>(this.api.buildUrl('/api/v1/rubros'));
  }

  /** @deprecated usar listarRubros */
  listarCategorias(): Observable<Rubro[]> {
    return this.listarRubros();
  }

  crear(articulo: ArticuloRequest): Observable<Articulo> {
    return this.http.post<Articulo>(this.api.buildUrl('/api/v1/articulos'), articulo);
  }

  actualizar(id: number, articulo: ArticuloRequest): Observable<Articulo> {
    return this.http.put<Articulo>(this.api.buildUrl(`/api/v1/articulos/${id}`), articulo);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(this.api.buildUrl(`/api/v1/articulos/${id}`));
  }

  buscarPorCodigo(codigoBarras: string): Observable<Articulo> {
    return this.http.get<Articulo>(this.api.buildUrl(`/api/v1/articulos/codigo/${encodeURIComponent(codigoBarras)}`));
  }

  alertasStockBajo(): Observable<Articulo[]> {
    return this.http.get<Articulo[]>(this.api.buildUrl('/api/v1/articulos/alertas/stock-bajo'));
  }
}
