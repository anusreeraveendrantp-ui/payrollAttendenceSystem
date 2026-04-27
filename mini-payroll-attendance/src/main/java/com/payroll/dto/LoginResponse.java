package com.payroll.dto;

import com.payroll.model.SystemRole;

import java.util.UUID;

public class LoginResponse {
    
    private String token;
    private UUID employeeId;
    private SystemRole systemRole;
    
    public LoginResponse() {
    }
    
    public LoginResponse(String token, UUID employeeId, SystemRole systemRole) {
        this.token = token;
        this.employeeId = employeeId;
        this.systemRole = systemRole;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public UUID getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(UUID employeeId) {
        this.employeeId = employeeId;
    }
    
    public SystemRole getSystemRole() {
        return systemRole;
    }
    
    public void setSystemRole(SystemRole systemRole) {
        this.systemRole = systemRole;
    }
}
