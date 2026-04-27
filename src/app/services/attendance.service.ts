import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AttendanceService {
  private apiUrl = `${environment.apiBaseUrl}/attendance`;

  constructor(private http: HttpClient) {}

  markAttendance(attendance: any): Observable<any> {
    return this.http.post(this.apiUrl, attendance);
  }

  getAttendanceByEmployee(employeeId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/employee/${employeeId}`);
  }
}
