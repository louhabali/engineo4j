export interface RegisterRequest {
  fullName: string;
  email: string;
  password: string;
}

export interface RegisterResponse {
  email: string;
  otpAuthUri: string;
}

export interface MfaVerifyRequest {
  email: string;
  code: string;
}

export interface MfaVerifyResponse {
  message: string;
}

export interface MfaLoginRequest {
  email: string;
  password: string;
  code: string;
}

export interface AuthResponse {
  token: string;
  tokenType: string;
}

export interface UserProfile {
  fullName: string;
  email: string;
  memberSince: string;
  totalWatchlist: number;
  totalFavorites: number;
  totalRatings: number;
}

export interface UserProfileUpdateRequest {
  fullName: string;
  email: string;
}

export interface WatchlistItem {
  id: number;
  userId: number;
  movieId: number;
  addedAt: string;
}

export interface WatchlistRequest {
  movieId: number;
}

export interface WatchlistStatusResponse {
  movieId: number;
  inWatchlist: boolean;
}

export interface ShareMovieRequest {
  movieId: number;
  friendEmail: string;
}

export interface ShareMovieResponse {
  message: string;
  shareId: number;
}

export interface ReceivedMovieShare {
  id: number;
  movieId: number;
  senderUserId: number;
  friendEmail: string;
  createdAt: string;
}
