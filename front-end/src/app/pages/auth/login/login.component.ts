import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { UserService } from '../../../core/services/user.service';
import { MfaLoginRequest } from '../../../models/user.model';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.component.html'
})
export class LoginComponent {
  email = '';
  password = '';
  code = ''; // 2FA Code field
  errorMessage = '';
  isLoading = false;

  constructor(
    private userService: UserService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  onLogin(): void {
    if (!this.email || !this.password || !this.code) {
      this.errorMessage = 'Please provide your email, password, and 6-digit MFA code.';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    const payload: MfaLoginRequest = {
      email: this.email,
      password: this.password,
      code: this.code
    };

    this.userService.login(payload).subscribe({
      next: (response) => {
        this.isLoading = false;
        console.log('Login successful, token securely cached.');
        const returnUrl = this.route.snapshot.queryParamMap.get('returnUrl');
        const safeReturnUrl = returnUrl?.startsWith('/') && !returnUrl.startsWith('//')
          ? returnUrl
          : '/catalog';
        void this.router.navigateByUrl(safeReturnUrl);
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = err.error?.message || 'Authentication failed. Check your credentials or MFA code.';
      }
    });
  }
}
