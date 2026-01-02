import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-verify-otp',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './verify-otp.component.html',
  styleUrl: './verify-otp.component.css'
})
export class VerifyOtpComponent implements OnInit {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);

  otpForm: FormGroup;
  loading = signal(false);
  errorMessage = signal('');
  successMessage = signal('');
  email = signal('');
  resendLoading = signal(false);

  constructor() {
    this.otpForm = this.fb.group({
      verificationCode: ['', [Validators.required, Validators.pattern(/^[0-9]{6}$/)]]
    });
  }

  ngOnInit(): void {
    const storedEmail = localStorage.getItem('pendingVerificationEmail') || '';
    this.email.set(storedEmail);
    // if (!storedEmail) {
    //   this.router.navigate(['/register']);
    // }
  }

  onSubmit(): void {
    if (this.otpForm.invalid) {
      this.otpForm.controls['verificationCode'].markAsTouched();
      return;
    }

    this.loading.set(true);
    this.errorMessage.set('');
    this.successMessage.set('');

    const data = {
      email: this.email(),
      verificationCode: this.otpForm.value.verificationCode
    };

    this.authService.verifyOtp(data).subscribe({
      next: (response) => {
        this.loading.set(false);
        this.successMessage.set('Email verified successfully! Redirecting to login...');
        localStorage.removeItem('pendingVerificationEmail');
        
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 2000);
      },
      error: (error) => {
        this.loading.set(false);
        this.errorMessage.set(error.error?.message || 'Invalid OTP. Please try again.');
      }
    });
  }

  resendOtp(): void {
    this.resendLoading.set(true);
    this.errorMessage.set('');
    this.successMessage.set('');

    this.authService.resendOtp(this.email()).subscribe({
      next: (response) => {
        this.resendLoading.set(false);
        this.successMessage.set('OTP sent successfully! Please check your email.');
      },
      error: (error) => {
        this.resendLoading.set(false);
        this.errorMessage.set(error.error?.message || 'Failed to resend OTP. Please try again.');
      }
    });
  }
}
