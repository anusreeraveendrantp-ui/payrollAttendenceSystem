import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = `${environment.apiBaseUrl}/auth`;

  constructor(private http: HttpClient) {}

  login(username: string, password: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, { username, password });
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('employeeId');
    localStorage.removeItem('systemRole');
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getEmployeeId(): string | null {
    return localStorage.getItem('employeeId');
  }

  getSystemRole(): string | null {
    return localStorage.getItem('systemRole');
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  isAdmin(): boolean {
    return this.getSystemRole() === 'ADMIN';
  }
}
