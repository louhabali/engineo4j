import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

import {
  RegisterRequest,
  RegisterResponse,
  MfaVerifyRequest,
  MfaVerifyResponse,
  MfaLoginRequest,
  AuthResponse,
  UserProfile,
  UserProfileUpdateRequest
} from '../../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private readonly apiUrl = 'http://localhost:8089/api/v1';

  constructor(private readonly http: HttpClient) {}

  register(data: RegisterRequest): Observable<RegisterResponse> {
    return this.http.post<RegisterResponse>(
      `${this.apiUrl}/auth/register`,
      data
    );
  }

  verifyMfa(data: MfaVerifyRequest): Observable<MfaVerifyResponse> {
    return this.http.post<MfaVerifyResponse>(
      `${this.apiUrl}/auth/mfa/verify`,
      data
    );
  }

  login(data: MfaLoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(
      `${this.apiUrl}/auth/login`,
      data
    ).pipe(
      tap((response: AuthResponse) => {
        if (response.token) {
          localStorage.setItem('auth_token', response.token);
        }
      })
    );
  }

  getUserProfile(): Observable<UserProfile> {
    return this.http.get<UserProfile>(
      `${this.apiUrl}/users/profile`
    );
  }

  updateUserProfile(
    profile: UserProfileUpdateRequest
  ): Observable<UserProfile> {
    return this.http.put<UserProfile>(
      `${this.apiUrl}/users/profile`,
      profile
    );
  }

  getToken(): string | null {
    return localStorage.getItem('auth_token');
  }

  logout(): void {
    localStorage.removeItem('auth_token');
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }
}
