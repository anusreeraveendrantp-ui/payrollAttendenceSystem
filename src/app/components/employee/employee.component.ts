import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { EmployeeService } from '../../services/employee.service';
import { EmployeeRole, SalaryType, SystemRole } from '../../models/enums';
import { BehaviorSubject } from 'rxjs';

@Component({
  selector: 'app-employee',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './employee.component.html',
  styleUrls: ['./employee.component.css']
})
export class EmployeeComponent implements OnInit {
  employees$ = new BehaviorSubject<any[]>([]);
  newEmployee = {
    name: '', role: EmployeeRole.OFFICE, salaryType: SalaryType.MONTHLY,
    salaryAmount: 0, systemRole: SystemRole.EMPLOYEE, username: '', password: ''
  };
  errorMessage = '';
  successMessage = '';
  isSubmitting = false;
  isLoading = false;
  employeeRoles = Object.values(EmployeeRole);
  salaryTypes = Object.values(SalaryType);
  systemRoles = Object.values(SystemRole);
showForm=false;
  constructor(private employeeService: EmployeeService) {}

  ngOnInit(): void {
    this.loadEmployees();
  }

  loadEmployees(): void {
    this.isLoading = true;
    this.errorMessage = '';
    this.employeeService.getAllEmployees().subscribe({
      next: (data: any[]) => {
        this.employees$.next(data);
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Failed to load employees';
        this.isLoading = false;
      }
    });
  }

  onSubmit(): void {
    this.errorMessage = '';
    this.successMessage = '';
    this.isSubmitting = true;
    this.showForm = false;
    this.employeeService.createEmployee(this.newEmployee).subscribe({
      next: () => {
        this.successMessage = 'Employee created successfully!';
        this.isSubmitting = false;
        this.resetForm();
        this.loadEmployees();
      },
      error: (error: any) => {
        this.isSubmitting = false;
        this.errorMessage = error.status === 409
          ? 'Username already exists.'
          : error.error?.message || 'Failed to create employee.';
      }
    });
  }

  resetForm(): void {
    this.newEmployee = {
      name: '', role: EmployeeRole.OFFICE, salaryType: SalaryType.MONTHLY,
      salaryAmount: 0, systemRole: SystemRole.EMPLOYEE, username: '', password: ''
    };
  }
}
