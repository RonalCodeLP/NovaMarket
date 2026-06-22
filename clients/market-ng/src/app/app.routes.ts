import { Routes } from '@angular/router';
import {
  articulosGuard,
  existenciasGuard,
  posGuard,
  rubrosGuard,
  ventasGuard,
} from './core/auth/role.guard';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'pos' },
  {
    path: 'auth',
    loadComponent: () => import('./auth/auth').then(m => m.Auth),
  },
  {
    path: 'pos',
    canActivate: [posGuard],
    loadComponent: () => import('./pos/pos').then(m => m.Pos),
  },
  {
    path: 'rubros',
    canActivate: [rubrosGuard],
    loadComponent: () => import('./categorias/categorias').then(m => m.Categorias),
  },
  { path: 'categorias', redirectTo: 'rubros', pathMatch: 'full' },
  {
    path: 'ventas',
    canActivate: [ventasGuard],
    loadComponent: () => import('./ventas-historial/ventas-historial').then(m => m.VentasHistorial),
  },
  {
    path: 'existencias',
    canActivate: [existenciasGuard],
    loadComponent: () => import('./inventario/inventario').then(m => m.Inventario),
  },
  { path: 'inventario', redirectTo: 'existencias', pathMatch: 'full' },
  {
    path: 'articulos',
    canActivate: [articulosGuard],
    loadComponent: () => import('./productos/productos').then(m => m.Productos),
  },
  { path: 'productos', redirectTo: 'articulos', pathMatch: 'full' },
  { path: '**', redirectTo: 'pos' },
];
