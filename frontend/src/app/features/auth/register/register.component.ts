import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  username = '';
  email = '';
  password = '';
  showPassword = false;
  error = '';
  isLoading = false;

  constructor(private authService: AuthService, private router: Router) {}

  onRegister() {
    if (!this.username || !this.email || !this.password) {
      this.error = 'All fields are required';
      return;
    }

    this.isLoading = true;
    this.error = '';

    this.authService.register(this.username, this.email, this.password).subscribe({
      next: (res) => {
        if (res && res.token) {
          this.authService.saveToken(res.token);
          this.authService.saveUser(this.username);
          this.router.navigate(['/tasks']);
        } else {
          this.router.navigate(['/auth/login']);
        }
      },
      error: (err) => {
        this.isLoading = false;
        if (err.error && err.error.message) {
          this.error = err.error.message;
        } else {
          this.error = 'Registration failed. Please try again.';
        }
      }
    });
  }
}
