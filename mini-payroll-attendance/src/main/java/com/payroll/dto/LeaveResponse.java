package com.payroll.dto;

import com.payroll.model.Leave;
import com.payroll.model.LeaveStatus;

import java.time.LocalDate;
import java.util.UUID;

public class LeaveResponse {
    
    private UUID id;
    private UUID employeeId;
    private String employeeName;
    private LocalDate startDate;
    private LocalDate endDate;
    private LeaveStatus status;
    private String reason;
    
    // Constructors
    public LeaveResponse() {
    }
    
    public LeaveResponse(UUID id, UUID employeeId, String employeeName, 
                        LocalDate startDate, LocalDate endDate, 
                        LeaveStatus status, String reason) {
        this.id = id;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.reason = reason;
    }
    
    // Factory method to create from Leave entity
    public static LeaveResponse fromEntity(Leave leave) {
        return new LeaveResponse(
            leave.getId(),
            leave.getEmployee().getId(),
            leave.getEmployee().getName(),
            leave.getStartDate(),
            leave.getEndDate(),
            leave.getStatus(),
            leave.getReason()
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
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    public LeaveStatus getStatus() {
        return status;
    }
    
    public void setStatus(LeaveStatus status) {
        this.status = status;
    }
    
    public String getReason() {
        return reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
}
