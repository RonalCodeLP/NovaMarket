import { Injectable, signal } from '@angular/core';
import Keycloak from 'keycloak-js';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private keycloak: Keycloak | null = null;
  private initPromise: Promise<boolean> | null = null;

  token = signal<string | null>(null);
  username = signal<string | null>(null);
  roles = signal<string[]>([]);

  init(): Promise<boolean> {
    if (this.initPromise) {
      return this.initPromise;
    }

    this.keycloak = new Keycloak({
      url: environment.keycloak.url,
      realm: environment.keycloak.realm,
      clientId: environment.keycloak.clientId,
    });

    this.initPromise = this.keycloak
      .init({
        onLoad: 'check-sso',
        pkceMethod: 'S256',
        checkLoginIframe: false,
      })
      .then(authenticated => {
        if (authenticated) {
          this.syncFromKeycloak();
        }
        return authenticated;
      })
      .catch(error => {
        console.error('Error al inicializar Keycloak', error);
        return false;
      });

    return this.initPromise;
  }

  isReady(): boolean {
    return this.keycloak != null;
  }

  isAuthenticated(): boolean {
    return this.keycloak?.authenticated === true;
  }

  async ensureAuthenticated(): Promise<boolean> {
    await this.init();
    if (!this.keycloak?.authenticated) {
      return false;
    }
    await this.updateTokenIfNeeded();
    return true;
  }

  login(returnUrl = '/pos'): Promise<void> {
    const redirectUri = `${window.location.origin}${returnUrl.startsWith('/') ? returnUrl : `/${returnUrl}`}`;
    return this.keycloak!.login({ redirectUri });
  }

  logout(returnUrl = '/'): Promise<void> {
    const redirectUri = `${window.location.origin}${returnUrl.startsWith('/') ? returnUrl : `/${returnUrl}`}`;
    this.clearSession();
    return this.keycloak!.logout({ redirectUri });
  }

  async updateTokenIfNeeded(minValiditySeconds = 30): Promise<void> {
    if (!this.keycloak?.authenticated) {
      return;
    }
    await this.keycloak.updateToken(minValiditySeconds);
    this.syncFromKeycloak();
  }

  hasRole(role: string): boolean {
    return this.roles().includes(role);
  }

  hasAnyRole(roles: string[]): boolean {
    return roles.some(role => this.hasRole(role));
  }

  private syncFromKeycloak(): void {
    if (!this.keycloak) {
      return;
    }

    this.token.set(this.keycloak.token ?? null);
    this.username.set(this.keycloak.tokenParsed?.['preferred_username'] as string ?? null);

    const rolesClaim = this.keycloak.tokenParsed?.['roles'];
    this.roles.set(Array.isArray(rolesClaim) ? rolesClaim as string[] : []);
  }

  private clearSession(): void {
    this.token.set(null);
    this.username.set(null);
    this.roles.set([]);
  }
}
