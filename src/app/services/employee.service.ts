import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {
  private apiUrl = `${environment.apiBaseUrl}/employees`;
   private selectedEmployee: any;

  constructor(private http: HttpClient) {}

  createEmployee(employee: any): Observable<any> {
    return this.http.post(this.apiUrl, employee);
  }

  getAllEmployees(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }
   setEmployee(emp: any) {
    this.selectedEmployee = emp;
  }

  getEmployee() {
    return this.selectedEmployee;
  }
}
