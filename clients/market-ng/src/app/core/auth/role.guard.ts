import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './auth.service';
import { RoleAccessService } from './role-access.service';
import { RolePermissions, defaultRoute } from './role-permissions.util';

function hasAnyAccess(permissions: RolePermissions): boolean {
  return (
    permissions.canAccessPos ||
    permissions.canAccessVentas ||
    permissions.canAccessExistencias ||
    permissions.canAccessArticulos ||
    permissions.canAccessRubros
  );
}

function guardRoute(check: (p: RolePermissions) => boolean, returnUrl: string): Promise<boolean> {
  const auth = inject(AuthService);
  const roles = inject(RoleAccessService);
  const router = inject(Router);

  return auth.ensureAuthenticated().then(ok => {
    if (!ok) {
      return router.navigate(['/auth'], { queryParams: { returnUrl } }).then(() => false);
    }

    const permissions = roles.permissions();
    if (!hasAnyAccess(permissions)) {
      return router
        .navigate(['/auth'], { queryParams: { sinAcceso: '1' } })
        .then(() => false);
    }

    if (check(permissions)) {
      return true;
    }

    const fallback = defaultRoute(permissions);
    if (fallback === '/auth') {
      return router
        .navigate(['/auth'], { queryParams: { sinAcceso: '1' } })
        .then(() => false);
    }

    return router.navigate([fallback]).then(() => false);
  });
}

export const posGuard: CanActivateFn = (_route, state) =>
  guardRoute(p => p.canAccessPos, state.url);

export const ventasGuard: CanActivateFn = (_route, state) =>
  guardRoute(p => p.canAccessVentas, state.url);

export const existenciasGuard: CanActivateFn = (_route, state) =>
  guardRoute(p => p.canAccessExistencias, state.url);

export const articulosGuard: CanActivateFn = (_route, state) =>
  guardRoute(p => p.canAccessArticulos, state.url);

export const rubrosGuard: CanActivateFn = (_route, state) =>
  guardRoute(p => p.canAccessRubros, state.url);
