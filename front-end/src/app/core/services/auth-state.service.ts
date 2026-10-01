import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { BehaviorSubject, Observable, catchError, finalize, map, of, shareReplay } from 'rxjs';
import { environment } from '../../../environments/environment.development';

const TOKEN_KEY = 'auth_token';

export interface SessionStatus {
  status: 'authenticated' | 'unauthenticated' | 'forbidden' | 'unavailable';
}

@Injectable({ providedIn: 'root' })
export class AuthStateService {
  private readonly authenticatedSubject = new BehaviorSubject<boolean>(false);
  readonly authenticated$ = this.authenticatedSubject.asObservable();

  private validationRequest: Observable<SessionStatus> | null = null;
  private readonly profileUrl = `${environment.apiBaseUrl}/users/profile`;

  constructor(private readonly http: HttpClient) {
    if (typeof window !== 'undefined') {
      window.addEventListener('storage', (event) => {
        if (event.key === TOKEN_KEY) {
          this.validationRequest = null;
          this.authenticatedSubject.next(false);
          if (this.getToken()) this.validateSession().subscribe();
        }
      });
    }
  }

  getToken(): string | null {
    if (typeof localStorage === 'undefined') return null;
    const token = localStorage.getItem(TOKEN_KEY);
    return token || null;
  }

  setToken(token: string): void {
    localStorage.setItem(TOKEN_KEY, token);
    this.validationRequest = null;
    this.authenticatedSubject.next(false);
  }

  logout(): void {
    if (typeof localStorage !== 'undefined') {
      localStorage.removeItem(TOKEN_KEY);
    }
    this.validationRequest = null;
    this.authenticatedSubject.next(false);
  }

  isAuthenticated(): boolean {
    return this.authenticatedSubject.value;
  }

  validateSession(): Observable<SessionStatus> {
    if (!this.getToken()) {
      this.authenticatedSubject.next(false);
      return of({ status: 'unauthenticated' });
    }

    if (this.validationRequest) return this.validationRequest;

    const request = this.http.get(this.profileUrl).pipe(
      map(() => {
        this.authenticatedSubject.next(true);
        return { status: 'authenticated' } as SessionStatus;
      }),
      catchError((error: HttpErrorResponse) => {
  
        return of({ status: 'unauthenticated' } as SessionStatus);
      }),
      finalize(() => {
        if (this.validationRequest === request) this.validationRequest = null;
      }),
      shareReplay({ bufferSize: 1, refCount: false }),
    );

    this.validationRequest = request;
    return request;
  }
}
