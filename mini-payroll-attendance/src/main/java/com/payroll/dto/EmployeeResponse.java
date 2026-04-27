package com.payroll.dto;

import com.payroll.model.Employee;
import com.payroll.model.EmployeeRole;
import com.payroll.model.SalaryType;
import com.payroll.model.SystemRole;

import java.math.BigDecimal;
import java.util.UUID;

public class EmployeeResponse {
    
    private UUID id;
    private String name;
    private EmployeeRole role;
    private SalaryType salaryType;
    private BigDecimal salaryAmount;
    private SystemRole systemRole;
    private String username;
    
    // Constructors
    public EmployeeResponse() {
    }
    
    public EmployeeResponse(UUID id, String name, EmployeeRole role, SalaryType salaryType,
                           BigDecimal salaryAmount, SystemRole systemRole, String username) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.salaryType = salaryType;
        this.salaryAmount = salaryAmount;
        this.systemRole = systemRole;
        this.username = username;
    }
    
    // Factory method to create from Employee entity
    public static EmployeeResponse fromEntity(Employee employee) {
        return new EmployeeResponse(
            employee.getId(),
            employee.getName(),
            employee.getRole(),
            employee.getSalaryType(),
            employee.getSalaryAmount(),
            employee.getSystemRole(),
            employee.getUsername()
        );
    }
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public EmployeeRole getRole() {
        return role;
    }
    
    public void setRole(EmployeeRole role) {
        this.role = role;
    }
    
    public SalaryType getSalaryType() {
        return salaryType;
    }
    
    public void setSalaryType(SalaryType salaryType) {
        this.salaryType = salaryType;
    }
    
    public BigDecimal getSalaryAmount() {
        return salaryAmount;
    }
    
    public void setSalaryAmount(BigDecimal salaryAmount) {
        this.salaryAmount = salaryAmount;
    }
    
    public SystemRole getSystemRole() {
        return systemRole;
    }
    
    public void setSystemRole(SystemRole systemRole) {
        this.systemRole = systemRole;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
}
