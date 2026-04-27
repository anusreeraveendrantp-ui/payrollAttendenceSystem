import { LeaveStatus } from './enums';

export interface ApplyLeaveRequest {
  employeeId: string;
  startDate: string;
  endDate: string;
  reason?: string;
}

export interface LeaveResponse {
  id: string;
  employeeId: string;
  employeeName: string;
  startDate: string;
  endDate: string;
  status: LeaveStatus;
  reason?: string;
}
