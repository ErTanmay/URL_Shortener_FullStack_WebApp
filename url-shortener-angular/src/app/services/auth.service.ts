import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject, tap } from 'rxjs';
import {
  RegisterRequest,
  LoginRequest,
  LoginResponse,
  OtpVerificationRequest,
  ForgotPasswordRequest,
  ResetPasswordRequest,
  UpdateUserRequest
} from '../models/models';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient);
  
  private readonly API_URL = environment.apiUrl;
  private readonly TOKEN_KEY = 'auth_token';
  private readonly USER_KEY = 'current_user';
  
  private currentUserSubject = new BehaviorSubject<any>(this.getUserFromStorage());
  public currentUser$ = this.currentUserSubject.asObservable();

  register(data: RegisterRequest): Observable<any> {
    return this.http.post(`${this.API_URL}/user/register`, data);
  }

  verifyOtp(data: OtpVerificationRequest): Observable<any> {
    return this.http.post(`${this.API_URL}/user/verify`, data, {responseType : 'text'});
  }

  resendOtp(email: string): Observable<any> {
    return this.http.post(`${this.API_URL}/user/resend`, { email });
  }

  login(data: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.API_URL}/user/login`, data).pipe(
      tap(response => {
        if (response.token) {
          this.setToken(response.token);
          // Store user info
          const userInfo = {
            username: data.username,
            loggedInAt: new Date().toISOString()
          };
          localStorage.setItem(this.USER_KEY, JSON.stringify(userInfo));
          this.currentUserSubject.next(userInfo);
        }
      })
    );
  }

  updateUser(data: UpdateUserRequest): Observable<any> {
    return this.http.put(`${this.API_URL}/user/update`, data);
  }

  forgotPasswordSendCode(data: ForgotPasswordRequest): Observable<any> {
    return this.http.post(`${this.API_URL}/user/forget-password/send-code`, data, {responseType:"text"});
  }

  forgotPasswordVerify(data: ResetPasswordRequest): Observable<any> {
    return this.http.post(`${this.API_URL}/user/forget-password/verify`, data, {responseType:"text"});
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
    this.currentUserSubject.next(null);
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  setToken(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
  }

  isAuthenticated(): boolean {
    const token = this.getToken();
    if (!token) return false;

    // Check if token is expired (15 min validity)
    const userInfo = this.getUserFromStorage();
    if (!userInfo || !userInfo.loggedInAt) return false;

    const loginTime = new Date(userInfo.loggedInAt).getTime();
    const currentTime = new Date().getTime();
    const fifteenMinutes = 15 * 60 * 1000;

    if (currentTime - loginTime > fifteenMinutes) {
      this.logout();
      return false;
    }

    return true;
  }

  private getUserFromStorage(): any {
    const userStr = localStorage.getItem(this.USER_KEY);
    return userStr ? JSON.parse(userStr) : null;
  }

  getCurrentUser(): any {
    return this.currentUserSubject.value;
  }
}
