import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../core/auth/auth.service';
import { RoleAccessService } from '../core/auth/role-access.service';

@Component({
  selector: 'app-auth',
  imports: [CommonModule],
  templateUrl: './auth.html',
  styleUrl: './auth.scss',
})
export class Auth implements OnInit {
  loading = signal(false);
  error = signal('');
  sinAcceso = signal(false);

  constructor(
    private auth: AuthService,
    private access: RoleAccessService,
    private router: Router,
    private route: ActivatedRoute,
  ) {}

  async ngOnInit() {
    await this.auth.init();

    if (this.route.snapshot.queryParamMap.get('sinAcceso') === '1') {
      this.sinAcceso.set(true);
      return;
    }

    if (!this.auth.isAuthenticated()) {
      return;
    }

    const destino = this.destinoTrasLogin();
    if (destino === '/auth') {
      this.sinAcceso.set(true);
      return;
    }

    await this.router.navigateByUrl(destino);
  }

  async loginWithKeycloak() {
    this.error.set('');
    this.sinAcceso.set(false);
    this.loading.set(true);

    try {
      const returnUrl = this.route.snapshot.queryParamMap.get('returnUrl') ?? '/pos';
      await this.auth.login(returnUrl);
    } catch {
      this.error.set(
        'No se pudo iniciar sesión. Intente nuevamente o contacte al administrador.',
      );
      this.loading.set(false);
    }
  }

  logout() {
    void this.auth.logout('/auth');
  }

  private destinoTrasLogin(): string {
    const returnUrl = this.route.snapshot.queryParamMap.get('returnUrl') ?? '/pos';
    const permisos = this.access.permissions();

    if (returnUrl.startsWith('/pos') && permisos.canAccessPos) {
      return '/pos';
    }
    if (returnUrl.startsWith('/ventas') && permisos.canAccessVentas) {
      return '/ventas';
    }
    if (returnUrl.startsWith('/existencias') && permisos.canAccessExistencias) {
      return '/existencias';
    }
    if (returnUrl.startsWith('/articulos') && permisos.canAccessArticulos) {
      return '/articulos';
    }
    if (returnUrl.startsWith('/rubros') && permisos.canAccessRubros) {
      return '/rubros';
    }

    return this.access.homeRoute();
  }
}
