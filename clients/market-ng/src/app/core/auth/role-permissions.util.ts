/** Permisos de pantalla al estilo minimarket (Plaza Vea / Tottus). */
export interface RolePermissions {
  canAccessPos: boolean;
  canAccessVentas: boolean;
  canAccessExistencias: boolean;
  canEditExistencias: boolean;
  canAccessArticulos: boolean;
  canEditArticulos: boolean;
  canDeleteArticulos: boolean;
  canAccessRubros: boolean;
  canEditRubros: boolean;
}

const ADMIN: RolePermissions = {
  canAccessPos: true,
  canAccessVentas: true,
  canAccessExistencias: true,
  canEditExistencias: true,
  canAccessArticulos: true,
  canEditArticulos: true,
  canDeleteArticulos: true,
  canAccessRubros: true,
  canEditRubros: true,
};

const SUPERVISOR: RolePermissions = {
  canAccessPos: true,
  canAccessVentas: true,
  canAccessExistencias: true,
  canEditExistencias: true,
  canAccessArticulos: true,
  canEditArticulos: true,
  canDeleteArticulos: false,
  canAccessRubros: true,
  canEditRubros: false,
};

const CAJERO: RolePermissions = {
  canAccessPos: true,
  canAccessVentas: true,
  canAccessExistencias: true,
  canEditExistencias: false,
  canAccessArticulos: false,
  canEditArticulos: false,
  canDeleteArticulos: false,
  canAccessRubros: false,
  canEditRubros: false,
};

const NONE: RolePermissions = {
  canAccessPos: false,
  canAccessVentas: false,
  canAccessExistencias: false,
  canEditExistencias: false,
  canAccessArticulos: false,
  canEditArticulos: false,
  canDeleteArticulos: false,
  canAccessRubros: false,
  canEditRubros: false,
};

const ROLE_LABELS: Record<string, string> = {
  ROLE_ADMIN: 'Administrador',
  ROLE_SUPERVISOR: 'Supervisor',
  ROLE_CAJERO: 'Cajero',
  ROLE_USER: 'Usuario',
  ROLE_REPARTIDOR: 'Repartidor',
};

const ROLE_PRIORITY = ['ROLE_ADMIN', 'ROLE_SUPERVISOR', 'ROLE_CAJERO', 'ROLE_REPARTIDOR', 'ROLE_USER'];

export function resolvePermissions(roles: string[]): RolePermissions {
  if (roles.includes('ROLE_ADMIN')) {
    return ADMIN;
  }
  if (roles.includes('ROLE_SUPERVISOR')) {
    return SUPERVISOR;
  }
  if (roles.includes('ROLE_CAJERO')) {
    return CAJERO;
  }
  return NONE;
}

export function primaryRoleLabel(roles: string[]): string {
  for (const role of ROLE_PRIORITY) {
    if (roles.includes(role)) {
      return ROLE_LABELS[role] ?? role.replace('ROLE_', '');
    }
  }
  return 'Sin rol';
}

export function primaryRoleClass(roles: string[]): string {
  if (roles.includes('ROLE_ADMIN')) {
    return 'role-admin';
  }
  if (roles.includes('ROLE_SUPERVISOR')) {
    return 'role-supervisor';
  }
  if (roles.includes('ROLE_CAJERO')) {
    return 'role-cajero';
  }
  return 'role-other';
}

export function defaultRoute(permissions: RolePermissions): string {
  if (permissions.canAccessPos) {
    return '/pos';
  }
  if (permissions.canAccessVentas) {
    return '/ventas';
  }
  if (permissions.canAccessExistencias) {
    return '/existencias';
  }
  if (permissions.canAccessArticulos) {
    return '/articulos';
  }
  if (permissions.canAccessRubros) {
    return '/rubros';
  }
  return '/auth';
}
