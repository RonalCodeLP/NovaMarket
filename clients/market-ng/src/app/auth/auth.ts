import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../core/auth/auth.service';

@Component({
  selector: 'app-auth',
  imports: [CommonModule],
  templateUrl: './auth.html',
  styleUrl: './auth.scss',
})
export class Auth implements OnInit {
  loading = signal(false);
  error = signal('');

  constructor(
    private auth: AuthService,
    private router: Router,
    private route: ActivatedRoute,
  ) {}

  async ngOnInit() {
    await this.auth.init();

    if (this.auth.isAuthenticated()) {
      const returnUrl = this.route.snapshot.queryParamMap.get('returnUrl') ?? '/pos';
      await this.router.navigateByUrl(returnUrl);
    }
  }

  async loginWithKeycloak() {
    this.error.set('');
    this.loading.set(true);

    try {
      const returnUrl = this.route.snapshot.queryParamMap.get('returnUrl') ?? '/pos';
      await this.auth.login(returnUrl);
    } catch {
      this.error.set(
        'No se pudo conectar con Keycloak (http://localhost:41880). Levante keycloak con .\\start-dev.ps1',
      );
      this.loading.set(false);
    }
  }
}
