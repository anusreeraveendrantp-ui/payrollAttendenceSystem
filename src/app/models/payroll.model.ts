import { SalaryType } from './enums';

export interface PayrollResponse {
  employeeId: string;
  employeeName: string;
  month: number;
  year: number;
  salaryType: SalaryType;
  salaryAmount: number;
  presentDays: number;
  approvedLeaveDays: number;
  payableDays: number;
  totalSalary: number;
}
