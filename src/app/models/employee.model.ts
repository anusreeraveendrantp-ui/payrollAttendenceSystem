import { EmployeeRole, SalaryType, SystemRole } from './enums';

export interface CreateEmployeeRequest {
  name: string;
  role: EmployeeRole;
  salaryType: SalaryType;
  salaryAmount: number;
  systemRole: SystemRole;
  username: string;
  password: string;
}

export interface EmployeeResponse {
  id: string;
  name: string;
  role: EmployeeRole;
  salaryType: SalaryType;
  salaryAmount: number;
  systemRole: SystemRole;
  username: string;
}
