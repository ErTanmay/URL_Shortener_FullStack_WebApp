import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'reset-password',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './reset-password.component.html',
  styleUrl: './reset-password.component.css'
})
export class ResetPasswordComponent implements OnInit {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);

  resetForm: FormGroup;
  loading = signal(false);
  errorMessage = signal('');
  successMessage = signal('');
  email = signal('');
  resendLoading = signal(false);

  constructor() {
    this.resetForm = this.fb.group({
      verificationCode: ['', [Validators.required, Validators.pattern(/^[0-9]{6}$/)]],
      password: ['', [Validators.required, Validators.minLength(8), 
        Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/)]]
    });
  }

  ngOnInit(): void {
    // Get email from localStorage
    const storedEmail = localStorage.getItem('resetPasswordEmail');
    if (!storedEmail) {
      // No email found, redirect back to forgot password page
      this.router.navigate(['/forgot-password']);
      return;
    }
    this.email.set(storedEmail);
  }

  onSubmit(): void {
    if (this.resetForm.invalid) {
      Object.keys(this.resetForm.controls).forEach(key => {
        this.resetForm.controls[key].markAsTouched();
      });
      return;
    }

    this.loading.set(true);
    this.errorMessage.set('');
    this.successMessage.set('');

    const data = {
      email: this.email(),
      verificationCode: this.resetForm.value.verificationCode,
      password: this.resetForm.value.password
    };

    this.authService.forgotPasswordVerify(data).subscribe({
      next: (response) => {
        this.loading.set(false);
        this.successMessage.set('Password reset successfully! Redirecting to login...');
        
        // Clear the stored email
        localStorage.removeItem('resetPasswordEmail');
        
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 2000);
      },
      error: (error) => {
        this.loading.set(false);
        this.errorMessage.set(error.error?.message || 'Failed to reset password. Please try again.');
      }
    });
  }

  resendCode(): void {
    this.resendLoading.set(true);
    this.errorMessage.set('');
    this.successMessage.set('');

    this.authService.forgotPasswordSendCode({ email: this.email() }).subscribe({
      next: (response) => {
        this.resendLoading.set(false);
        this.successMessage.set('Verification code sent again! Please check your email.');
      },
      error: (error) => {
        this.resendLoading.set(false);
        this.errorMessage.set(error.error?.message || 'Failed to resend code. Please try again.');
      }
    });
  }
}