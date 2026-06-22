import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiService } from '../core/services/api.service';

export interface Rubro {
  id: number;
  nombre: string;
  descripcion: string;
}

export interface RubroRequest {
  nombre: string;
  descripcion: string;
}

/** @deprecated usar Rubro */
export type Categoria = Rubro;
/** @deprecated usar RubroRequest */
export type CategoriaRequest = RubroRequest;

@Injectable({ providedIn: 'root' })
export class CategoriasService {
  constructor(
    private http: HttpClient,
    private api: ApiService,
  ) {}

  listar(): Observable<Rubro[]> {
    return this.http.get<Rubro[]>(this.api.buildUrl('/api/v1/rubros'));
  }

  crear(rubro: RubroRequest): Observable<Rubro> {
    return this.http.post<Rubro>(this.api.buildUrl('/api/v1/rubros'), rubro);
  }

  actualizar(id: number, rubro: RubroRequest): Observable<Rubro> {
    return this.http.put<Rubro>(this.api.buildUrl(`/api/v1/rubros/${id}`), rubro);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(this.api.buildUrl(`/api/v1/rubros/${id}`));
  }
}
