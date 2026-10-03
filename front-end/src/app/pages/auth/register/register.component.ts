import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';

import { UserService } from '../../../core/services/user.service';
import {
  RegisterRequest,
  RegisterResponse,
  MfaVerifyRequest
} from '../../../models/user.model';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './register.component.html'
})
export class RegisterComponent {

  fullName = '';
  email = '';
  password = '';

  errorMessage = '';
  mfaErrorMessage = '';

  isLoading = false;
  isMfaLoading = false;

  showMfaPopup = false;

  otpAuthUri = ''; // Changed from qrCodeImageUri
  mfaCode = '';

  constructor(
    private userService: UserService,
    private router: Router,
    private sanitizer: DomSanitizer // Inject sanitizer
  ) { }

  onRegister(): void {
    if (!this.fullName || !this.email || !this.password) {
      this.errorMessage = 'Please complete all required fields.';
      return;
    }

    this.errorMessage = '';
    this.isLoading = true;

    const payload: RegisterRequest = {
      fullName: this.fullName,
      email: this.email,
      password: this.password
    };

    this.userService.register(payload).subscribe({
      next: (response: RegisterResponse) => {
        this.isLoading = false;

        // Match the property coming from your Java RegisterResponse DTO
        console.log('OTP AUTH URI:', response.otpAuthUri);
        this.otpAuthUri = response.otpAuthUri;

        this.showMfaPopup = true;
      },
      error: (err) => {
        this.isLoading = false;
        console.log("[ERROR In REGISTER] ==> ", err)
        this.errorMessage =
          err.error?.errorMessage ||
          'Registration failed. Please try again.';
      }
    });
  }

  // Safe method to generate the QR code image URL for the HTML template
  getSafeQrUrl(): SafeUrl {
    const qrApiUrl = 'https://api.qrserver.com/v1/create-qr-code/?size=220x220&data=' + encodeURIComponent(this.otpAuthUri);
    return this.sanitizer.bypassSecurityTrustUrl(qrApiUrl);
  }

  verifyMfa(): void {
    if (!this.mfaCode || this.mfaCode.length !== 6) {
      this.mfaErrorMessage = 'Please enter the 6-digit verification code.';
      return;
    }

    this.mfaErrorMessage = '';
    this.isMfaLoading = true;

    const payload: MfaVerifyRequest = {
      email: this.email,
      code: this.mfaCode
    };

    this.userService.verifyMfa(payload).subscribe({
      next: () => {
        this.isMfaLoading = false;
        this.showMfaPopup = false;
        this.router.navigate(['/login']);
      },
      error: (err) => {
        this.isMfaLoading = false;
        this.mfaErrorMessage =
          err.error?.message ||
          'Invalid MFA code. Please try again.';
      }
    });
  }

  // skipMfa(): void {
  //   this.showMfaPopup = false;
  //   this.router.navigate(['/login']);
  // }
}