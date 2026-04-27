import { SystemRole } from './enums';

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  employeeId: string;
  systemRole: SystemRole;
}
