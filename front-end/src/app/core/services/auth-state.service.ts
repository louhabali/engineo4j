import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

const TOKEN_KEY = 'auth_token';

@Injectable({ providedIn: 'root' })
export class AuthStateService {
  private readonly authenticatedSubject = new BehaviorSubject<boolean>(false);
  readonly authenticated$ = this.authenticatedSubject.asObservable();

  constructor() {
    this.authenticatedSubject.next(this.getToken() !== null);

    if (typeof window !== 'undefined') {
      window.addEventListener('storage', (event) => {
        if (event.key === TOKEN_KEY) {
          this.authenticatedSubject.next(this.getToken() !== null);
        }
      });
    }
  }

  getToken(): string | null {
    if (typeof localStorage === 'undefined') return null;

    const token = localStorage.getItem(TOKEN_KEY);
    if (!token) return null;

    if (!this.isTokenUnexpired(token)) {
      this.logout();
      return null;
    }

    return token;
  }

  setToken(token: string): void {
    localStorage.setItem(TOKEN_KEY, token);
    this.authenticatedSubject.next(this.isTokenUnexpired(token));
  }

  logout(): void {
    if (typeof localStorage !== 'undefined') {
      localStorage.removeItem(TOKEN_KEY);
    }
    this.authenticatedSubject.next(false);
  }

  isAuthenticated(): boolean {
    return this.getToken() !== null;
  }

  private isTokenUnexpired(token: string): boolean {
    try {
      const encodedPayload = token.split('.')[1];
      if (!encodedPayload) return false;

      const base64Payload = encodedPayload
        .replace(/-/g, '+')
        .replace(/_/g, '/');
      const paddedPayload = base64Payload.padEnd(
        Math.ceil(base64Payload.length / 4) * 4,
        '='
      );
      const claims = JSON.parse(atob(paddedPayload)) as { exp?: number };

      return typeof claims.exp === 'number' && claims.exp * 1000 > Date.now();
    } catch {
      return false;
    }
  }
}
