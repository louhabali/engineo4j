import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of, switchMap, tap, throwError } from 'rxjs';
import { AuthStateService } from './auth-state.service';
import { environment } from '../../../environments/environment.development';

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

  private readonly apiUrl = environment.apiBaseUrl;
  readonly authenticated$: Observable<boolean>;

  constructor(
    private readonly http: HttpClient,
    private readonly authState: AuthStateService
  ) {
    this.authenticated$ = authState.authenticated$;
  }

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
          this.authState.setToken(response.token);
        }
      }),
      switchMap((response) => this.authState.validateSession().pipe(
        switchMap((sessionStatus) => sessionStatus.status === 'authenticated'
          ? of(response)
          : throwError(() => new Error('The backend could not validate the new session.')),
        ),
      )),
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
    return this.authState.getToken();
  }

  logout(): void {
    this.authState.logout();
  }

  isAuthenticated(): boolean {
    return this.authState.isAuthenticated();
  }
}
