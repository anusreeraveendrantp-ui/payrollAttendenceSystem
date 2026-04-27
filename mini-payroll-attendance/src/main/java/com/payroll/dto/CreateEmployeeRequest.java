package com.payroll.dto;

import com.payroll.model.EmployeeRole;
import com.payroll.model.SalaryType;
import com.payroll.model.SystemRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class CreateEmployeeRequest {
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotNull(message = "Employee role is required")
    private EmployeeRole role;
    
    @NotNull(message = "Salary type is required")
    private SalaryType salaryType;
    
    @NotNull(message = "Salary amount is required")
    @Positive(message = "Salary amount must be positive")
    private BigDecimal salaryAmount;
    
    @NotNull(message = "System role is required")
    private SystemRole systemRole;
    
    @NotBlank(message = "Username is required")
    private String username;
    
    @NotBlank(message = "Password is required")
    private String password;
    
    // Constructors
    public CreateEmployeeRequest() {
    }
    
    public CreateEmployeeRequest(String name, EmployeeRole role, SalaryType salaryType,
                                BigDecimal salaryAmount, SystemRole systemRole,
                                String username, String password) {
        this.name = name;
        this.role = role;
        this.salaryType = salaryType;
        this.salaryAmount = salaryAmount;
        this.systemRole = systemRole;
        this.username = username;
        this.password = password;
    }
    
    // Getters and Setters
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
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}
