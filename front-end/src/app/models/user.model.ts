export interface RegisterRequest {
  fullName: string;
  email: string;
  password: string;
}

export interface RegisterResponse {
  email: string;
    otpAuthUri: string; // Changed from qrCodeImageUri
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