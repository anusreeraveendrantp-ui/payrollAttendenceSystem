import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { LeaveService } from '../../services/leave.service';
import { AuthService } from '../../services/auth.service';
import { LeaveStatus } from '../../models/enums';
import { BehaviorSubject } from 'rxjs';

@Component({
  selector: 'app-leave',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './leave.component.html',
  styleUrls: ['./leave.component.css']
})
export class LeaveComponent implements OnInit {
  isAdmin = false;
  currentEmployeeId = '';
  startDate = '';
  endDate = '';
  reason = '';
  leaveRecords$ = new BehaviorSubject<any[]>([]);
  errorMessage = '';
  successMessage = '';
  isSubmitting = false;
  isLoading = false;
  LeaveStatus = LeaveStatus;

  constructor(private leaveService: LeaveService, private authService: AuthService) {}

  ngOnInit(): void {
    this.isAdmin = this.authService.isAdmin();
    this.currentEmployeeId = this.authService.getEmployeeId() || '';
    if (this.isAdmin) { this.loadAllLeaves(); } else { this.loadOwnLeaves(); }
  }

  loadAllLeaves(): void {
    this.isLoading = true;
    this.leaveService.getAllLeaves().subscribe({
      next: (data) => {
        this.leaveRecords$.next(data.sort((a: any, b: any) =>
          new Date(b.startDate).getTime() - new Date(a.startDate).getTime()));
        this.isLoading = false;
      },
      error: () => { this.errorMessage = 'Failed to load leave records'; this.isLoading = false; }
    });
  }

  loadOwnLeaves(): void {
    if (!this.currentEmployeeId) return;
    this.isLoading = true;
    this.leaveService.getLeavesByEmployee(this.currentEmployeeId).subscribe({
      next: (data) => {
        this.leaveRecords$.next(data.sort((a: any, b: any) =>
          new Date(b.startDate).getTime() - new Date(a.startDate).getTime()));
        this.isLoading = false;
      },
      error: () => { this.errorMessage = 'Failed to load leave records'; this.isLoading = false; }
    });
  }

  onSubmit(): void {
    if (!this.startDate || !this.endDate) {
      this.errorMessage = 'Please provide both start and end date'; return;
    }
    this.errorMessage = '';
    this.successMessage = '';
    this.isSubmitting = true;
    this.leaveService.applyLeave({
      employeeId: this.currentEmployeeId, startDate: this.startDate,
      endDate: this.endDate, reason: this.reason || undefined
    }).subscribe({
      next: () => {
        this.successMessage = 'Leave application submitted!';
        this.isSubmitting = false;
        this.startDate = ''; this.endDate = ''; this.reason = '';
        this.loadOwnLeaves();
      },
      error: (error: any) => {
        this.isSubmitting = false;
        this.errorMessage = error.error?.message || 'Failed to apply for leave.';
      }
    });
  }

  approveLeave(leaveId: string): void {
    this.leaveService.approveLeave(leaveId).subscribe({
      next: () => { this.successMessage = 'Leave approved!'; this.loadAllLeaves(); },
      error: (error: any) => {
        this.errorMessage = error.status === 400
          ? 'Only PENDING leaves can be approved.' : 'Failed to approve leave.';
      }
    });
  }

  rejectLeave(leaveId: string): void {
    this.leaveService.rejectLeave(leaveId).subscribe({
      next: () => { this.successMessage = 'Leave rejected!'; this.loadAllLeaves(); },
      error: (error: any) => {
        this.errorMessage = error.status === 400
          ? 'Only PENDING leaves can be rejected.' : 'Failed to reject leave.';
      }
    });
  }

  formatDate(d: string): string {
    return new Date(d).toLocaleDateString('en-IN', { year: 'numeric', month: 'short', day: 'numeric' });
  }

  getStatusClass(status: LeaveStatus): string {
    return status === LeaveStatus.PENDING ? 'badge-pending'
      : status === LeaveStatus.APPROVED ? 'badge-approved' : 'badge-rejected';
  }
}
