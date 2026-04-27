import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class LeaveService {
  private apiUrl = `${environment.apiBaseUrl}/leaves`;

  constructor(private http: HttpClient) {}

  applyLeave(leave: any): Observable<any> {
    return this.http.post(this.apiUrl, leave);
  }

  approveLeave(leaveId: string): Observable<any> {
    return this.http.put(`${this.apiUrl}/${leaveId}/approve`, {});
  }

  rejectLeave(leaveId: string): Observable<any> {
    return this.http.put(`${this.apiUrl}/${leaveId}/reject`, {});
  }

  getAllLeaves(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  getLeavesByEmployee(employeeId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/employee/${employeeId}`);
  }
}
