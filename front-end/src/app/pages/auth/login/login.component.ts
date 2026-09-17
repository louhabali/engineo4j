import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.component.html'
})
export class LoginComponent {
  email = '';
  password = '';
  errorMessage = '';

  constructor(private router: Router) {}

  onLogin(): void {
    if (!this.email || !this.password) {
      this.errorMessage = 'Please provide valid credentials.';
      return;
    }

    // TODO: Connect to backend Spring Boot JWT authentication endpoint
    console.log('Logging in user:', { email: this.email });

    // Navigate to catalog or recommendations on success
    this.router.navigate(['/catalog']);
  }
}