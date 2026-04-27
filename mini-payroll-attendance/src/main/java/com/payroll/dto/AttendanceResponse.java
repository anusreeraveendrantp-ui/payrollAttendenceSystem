package com.payroll.dto;

import com.payroll.model.Attendance;
import com.payroll.model.AttendanceStatus;

import java.time.LocalDate;
import java.util.UUID;

public class AttendanceResponse {
    
    private UUID id;
    private UUID employeeId;
    private String employeeName;
    private LocalDate date;
    private AttendanceStatus status;
    
    // Constructors
    public AttendanceResponse() {
    }
    
    public AttendanceResponse(UUID id, UUID employeeId, String employeeName, 
                             LocalDate date, AttendanceStatus status) {
        this.id = id;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.date = date;
        this.status = status;
    }
    
    // Factory method to create from Attendance entity
    public static AttendanceResponse fromEntity(Attendance attendance) {
        return new AttendanceResponse(
            attendance.getId(),
            attendance.getEmployee().getId(),
            attendance.getEmployee().getName(),
            attendance.getDate(),
            attendance.getStatus()
        );
    }
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public UUID getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(UUID employeeId) {
        this.employeeId = employeeId;
    }
    
    public String getEmployeeName() {
        return employeeName;
    }
    
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
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
