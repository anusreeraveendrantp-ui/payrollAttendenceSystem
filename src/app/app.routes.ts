import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';
import { roleGuard } from './guards/role.guard';
import { PayrollComponent } from './components/payroll/payroll.component';
import { LoginComponent } from './components/login/login.component';
import { EmployeeComponent } from './components/employee/employee.component';
import { AttendanceComponent } from './components/attendance/attendance.component';
import { LeaveComponent } from './components/leave/leave.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'employees', component: EmployeeComponent, canActivate: [authGuard, roleGuard], data: { role: 'ADMIN' } },
  { path: 'attendance', component: AttendanceComponent, canActivate: [authGuard] },
  { path: 'leave', component: LeaveComponent, canActivate: [authGuard] },
  { path: 'payroll', component: PayrollComponent, canActivate: [authGuard] },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: '**', redirectTo: '/login' }
];
