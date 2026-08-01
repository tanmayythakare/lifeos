import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  username = '';
  password = '';
  showPassword = false;
  error = '';
  isLoading = false;

  constructor(private authService: AuthService, private router: Router) {}

  onLogin() {
    if (!this.username || !this.password) {
      this.error = 'Please enter both username and password';
      return;
    }

    this.isLoading = true;
    this.error = '';

    this.authService.login(this.username, this.password).subscribe({
      next: (res) => {
        this.authService.saveToken(res.token);
        this.authService.saveUser(this.username);
        this.router.navigate(['/tasks']);
      },
      error: (err) => {
        this.isLoading = false;
        if (err.status === 401 || err.status === 400) {
          this.error = 'Invalid username or password';
        } else {
          this.error = 'Login failed. Please try again.';
        }
      }
    });
  }
}
