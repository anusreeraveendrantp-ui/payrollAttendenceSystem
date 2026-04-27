import { AttendanceStatus } from './enums';

export interface MarkAttendanceRequest {
  employeeId: string;
  date: string;
  status: AttendanceStatus;
}

export interface AttendanceResponse {
  id: string;
  employeeId: string;
  employeeName: string;
  date: string;
  status: AttendanceStatus;
}
