package com.payroll.dto;

import com.payroll.model.AttendanceStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public class MarkAttendanceRequest {
    
    @NotNull(message = "Employee ID is required")
    private UUID employeeId;
    
    @NotNull(message = "Date is required")
    private LocalDate date;
    
    @NotNull(message = "Status is required")
    private AttendanceStatus status;
    
    // Constructors
    public MarkAttendanceRequest() {
    }
    
    public MarkAttendanceRequest(UUID employeeId, LocalDate date, AttendanceStatus status) {
        this.employeeId = employeeId;
        this.date = date;
        this.status = status;
    }
    
    // Getters and Setters
    public UUID getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(UUID employeeId) {
        this.employeeId = employeeId;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public AttendanceStatus getStatus() {
        return status;
    }
    
    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }
}
