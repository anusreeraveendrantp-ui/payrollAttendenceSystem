import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  username = '';
  password = '';
  errorMessage = '';
  isLoading = false;

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit(): void {
    if (!this.username || !this.password) {
      this.errorMessage = 'Username and password are required';
      return;
    }
    this.isLoading = true;
    this.errorMessage = '';
    this.authService.login(this.username, this.password).subscribe({
      next: (response: any) => {
        const role = String(response.systemRole);
        localStorage.setItem('token', response.token);
        localStorage.setItem('employeeId', String(response.employeeId));
        localStorage.setItem('systemRole', role);
        this.isLoading = false;
        setTimeout(() => {
          if (role === 'ADMIN') {
            this.router.navigate(['/employees']);
          } else {
            this.router.navigate(['/attendance']);
          }
        }, 100);
      },
      error: (error: any) => {
        this.isLoading = false;
        this.errorMessage = error.status === 401
          ? 'Invalid username or password'
          : 'An error occurred. Please try again.';
      }
    });
  }
}
