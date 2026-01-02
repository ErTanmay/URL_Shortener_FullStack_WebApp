// User models
export interface User {
  fName: string;
  lName: string;
  email: string;
  phone: string;
  username: string;
}

export interface RegisterRequest extends User {
  password: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  message?: string;
}

export interface OtpVerificationRequest {
  email: string;
  verificationCode: string;
}

export interface UpdateUserRequest extends User {
  password: string;
}

// URL models
export interface UrlShortenerRequest {
  longUrl: string;
}

export interface UrlShortenerResponse {
  id: number;
  shortUrl: string;
  longUrl: string;
  createdAt?: string;
  clickCount?: number;
}

// Forgot password models
export interface ForgotPasswordRequest {
  email: string;
}

export interface ResetPasswordRequest {
  email: string;
  verificationCode: string;
  password: string;
}

// API Response wrapper
export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data?: T;
}
