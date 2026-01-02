import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-forgot-password',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './forgot-password.component.html',
  styleUrl: './forgot-password.component.css'
})
export class ForgotPasswordComponent {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);

  emailForm: FormGroup;
  loading = signal(false);
  errorMessage = signal('');

  constructor() {
    this.emailForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]]
    });
  }

  sendCode(): void {
    if (this.emailForm.invalid) {
      this.emailForm.controls['email'].markAsTouched();
      return;
    }

    this.loading.set(true);
    this.errorMessage.set('');

    this.authService.forgotPasswordSendCode(this.emailForm.value).subscribe({
      next: (response) => {
        this.loading.set(false);
        
        // Store email in localStorage for next page
        localStorage.setItem('resetPasswordEmail', this.emailForm.value.email);
        
        // Redirect to reset password page
        this.router.navigate(['/reset-password']);
      },
      error: (error) => {
        this.loading.set(false);
        this.errorMessage.set(error.error?.message || 'Failed to send verification code. Please try again.');
      }
    });
  }
}