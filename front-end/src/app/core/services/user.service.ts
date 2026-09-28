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
  UserProfileUpdateRequest,
  WatchlistItem,
  WatchlistStatusResponse
} from '../../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private readonly apiUrl = 'http://localhost:8089/api/v1';

  constructor(private readonly http: HttpClient) {}

  // --- Authentication ---

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

  // --- User Profile ---

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

  // --- Watchlist ---

  getWatchlistMovieIds(): Observable<number[]> {
    return this.http.get<number[]>(
      `${this.apiUrl}/users/watchlist`
    );
  }

  getWatchlistItems(): Observable<WatchlistItem[]> {
    return this.http.get<WatchlistItem[]>(
      `${this.apiUrl}/users/watchlist/items`
    );
  }

  addToWatchlist(movieId: number): Observable<void> {
    return this.http.post<void>(
      `${this.apiUrl}/users/watchlist/${movieId}`,
      {}
    );
  }

  removeFromWatchlist(movieId: number): Observable<void> {
    return this.http.delete<void>(
      `${this.apiUrl}/users/watchlist/${movieId}`
    );
  }

isInWatchlist(movieId: number): Observable<boolean> {
  return this.http.get<boolean>(
    `${this.apiUrl}/users/watchlist/check/${movieId}`
  );
}


  // --- Auth Helpers ---

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