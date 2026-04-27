import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AttendanceService } from '../../services/attendance.service';
import { EmployeeService } from '../../services/employee.service';
import { AuthService } from '../../services/auth.service';
import { AttendanceStatus } from '../../models/enums';
import { BehaviorSubject } from 'rxjs';

@Component({
  selector: 'app-attendance',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './attendance.component.html',
  styleUrls: ['./attendance.component.css']
})
export class AttendanceComponent implements OnInit {
  isAdmin = false;
  currentEmployeeId = '';

  employees$ = new BehaviorSubject<any[]>([]);
  attendanceRecords$ = new BehaviorSubject<any[]>([]);

  selectedEmployeeId = '';
  selectedDate = '';
  selectedStatus: AttendanceStatus = AttendanceStatus.PRESENT;
  errorMessage = '';
  successMessage = '';
  isSubmitting = false;
  isLoadingEmployees = false;
  isLoadingRecords = false;
  attendanceStatuses = Object.values(AttendanceStatus);

  constructor(
    private attendanceService: AttendanceService,
    private employeeService: EmployeeService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.isAdmin = this.authService.isAdmin();
    this.currentEmployeeId = this.authService.getEmployeeId() || '';
    this.selectedDate = this.getTodayDate();

    if (this.isAdmin) {
      this.loadEmployees();
    } else {
      this.loadAttendance(this.currentEmployeeId);
    }
  }

  loadEmployees(): void {
    this.isLoadingEmployees = true;
    this.employeeService.getAllEmployees().subscribe({
      next: (data) => {
        this.employees$.next(data);
        this.isLoadingEmployees = false;
      },
      error: () => {
        this.errorMessage = 'Failed to load employees';
        this.isLoadingEmployees = false;
      }
    });
  }

  loadAttendance(employeeId: string): void {
    this.isLoadingRecords = true;
    this.attendanceService.getAttendanceByEmployee(employeeId).subscribe({
      next: (data) => {
        const sorted = data.sort((a: any, b: any) =>
          new Date(b.date).getTime() - new Date(a.date).getTime());
        this.attendanceRecords$.next(sorted);
        this.isLoadingRecords = false;
      },
      error: () => {
        this.errorMessage = 'Failed to load attendance records';
        this.isLoadingRecords = false;
      }
    });
  }

  onEmployeeChange(): void {
    this.errorMessage = '';
    this.successMessage = '';
    if (this.selectedEmployeeId) {
      this.loadAttendance(this.selectedEmployeeId);
    } else {
      this.attendanceRecords$.next([]);
    }
  }

  onSubmit(): void {
    if (!this.selectedEmployeeId || !this.selectedDate) {
      this.errorMessage = 'Please select an employee and date';
      return;
    }
    this.errorMessage = '';
    this.successMessage = '';
    this.isSubmitting = true;

    this.attendanceService.markAttendance({
      employeeId: this.selectedEmployeeId,
      date: this.selectedDate,
      status: this.selectedStatus
    }).subscribe({
      next: () => {
        this.successMessage = 'Attendance marked successfully!';
        this.isSubmitting = false;
        this.loadAttendance(this.selectedEmployeeId);
      },
      error: (error: any) => {
        this.isSubmitting = false;
        this.errorMessage = error.status === 409
          ? 'Attendance already marked for this date.'
          : error.error?.message || 'Failed to mark attendance.';
      }
    });
  }

  getTodayDate(): string {
    const t = new Date();
    return `${t.getFullYear()}-${String(t.getMonth() + 1).padStart(2, '0')}-${String(t.getDate()).padStart(2, '0')}`;
  }

  formatDate(d: string): string {
    return new Date(d).toLocaleDateString('en-IN', {
      year: 'numeric', month: 'short', day: 'numeric'
    });
  }
}
