import { computed, Injectable, inject } from '@angular/core';
import { AuthService } from './auth.service';
import {
  defaultRoute,
  primaryRoleClass,
  primaryRoleLabel,
  resolvePermissions,
  RolePermissions,
} from './role-permissions.util';

@Injectable({ providedIn: 'root' })
export class RoleAccessService {
  private readonly auth = inject(AuthService);

  readonly permissions = computed(() => resolvePermissions(this.auth.roles()));

  readonly roleLabel = computed(() => primaryRoleLabel(this.auth.roles()));

  readonly roleClass = computed(() => primaryRoleClass(this.auth.roles()));

  readonly homeRoute = computed(() => defaultRoute(this.permissions()));

  can(check: (p: RolePermissions) => boolean): boolean {
    return check(this.permissions());
  }
}
