import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

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

  constructor(private router: Router) {}

  onRegister(): void {
    if (!this.fullName || !this.email || !this.password) {
      this.errorMessage = 'Please complete all required fields.';
      return;
    }

    // TODO: Connect to backend Spring Boot user registration API
    console.log('Registering new node:', { name: this.fullName, email: this.email });

    // Navigate to login or main catalog page after registration
    this.router.navigate(['/auth/login']);
  }
}