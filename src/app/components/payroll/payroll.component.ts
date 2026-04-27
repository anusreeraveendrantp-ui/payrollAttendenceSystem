import { Component, OnInit } from '@angular/core';
import { CommonModule, AsyncPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PayrollService } from '../../services/payroll.service';
import { EmployeeService } from '../../services/employee.service';
import { AuthService } from '../../services/auth.service';
import { BehaviorSubject } from 'rxjs';

@Component({
  selector: 'app-payroll',
  standalone: true,
  imports: [CommonModule, FormsModule, AsyncPipe],
  templateUrl: './payroll.component.html',
  styleUrls: ['./payroll.component.css']
})
export class PayrollComponent implements OnInit {
  isAdmin = false;
  currentEmployeeId = '';
  employees$ = new BehaviorSubject<any[]>([]);
  payrollData$ = new BehaviorSubject<any>(null);

  selectedEmployeeId = '';
  selectedMonth: number = new Date().getMonth() + 1;
  selectedYear: number = new Date().getFullYear();
  errorMessage = '';
  isLoading = false;

  months = [
    { value: 1,  label: 'January'   }, { value: 2,  label: 'February'  },
    { value: 3,  label: 'March'     }, { value: 4,  label: 'April'     },
    { value: 5,  label: 'May'       }, { value: 6,  label: 'June'      },
    { value: 7,  label: 'July'      }, { value: 8,  label: 'August'    },
    { value: 9,  label: 'September' }, { value: 10, label: 'October'   },
    { value: 11, label: 'November'  }, { value: 12, label: 'December'  }
  ];
  years: number[] = [];

  constructor(
    private payrollService: PayrollService,
    private employeeService: EmployeeService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.isAdmin = this.authService.isAdmin();
    this.currentEmployeeId = this.authService.getEmployeeId() || '';
    const y = new Date().getFullYear();
    this.years = [y, y - 1, y - 2];
    this.selectedYear = y;
    this.selectedMonth = new Date().getMonth() + 1;
    if (this.isAdmin) { this.loadEmployees(); }
  }

  loadEmployees(): void {
    this.employeeService.getAllEmployees().subscribe({
      next: (data) => { this.employees$.next(data); },
      error: () => { this.errorMessage = 'Failed to load employees'; }
    });
  }

  onSubmit(): void {
    const employeeId = this.isAdmin ? this.selectedEmployeeId : this.currentEmployeeId;
    if (!employeeId) {
      this.errorMessage = this.isAdmin ? 'Please select an employee' : 'Employee ID not found';
      return;
    }

    // Ensure month and year are numbers
    const month = Number(this.selectedMonth);
    const year = Number(this.selectedYear);

    this.errorMessage = '';
    this.payrollData$.next(null);
    this.isLoading = true;

    this.payrollService.getPayroll(employeeId, month, year).subscribe({
      next: (data) => {
        this.payrollData$.next(data);
        this.isLoading = false;
      },
      error: (error: any) => {
        this.isLoading = false;
        this.errorMessage = error.error?.message || 'Failed to load payroll data.';
      }
    });
  }

  getMonthName(month: number): string {
    return this.months.find(m => m.value === Number(month))?.label || '';
  }
}
