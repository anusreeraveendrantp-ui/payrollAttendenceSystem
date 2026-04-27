package com.payroll.dto;

import com.payroll.model.SalaryType;

import java.math.BigDecimal;
import java.util.UUID;

public class PayrollResponse {
    
    private UUID employeeId;
    private String employeeName;
    private int month;
    private int year;
    private SalaryType salaryType;
    private BigDecimal salaryAmount;
    private int presentDays;
    private int approvedLeaveDays;
    private int payableDays;
    private BigDecimal totalSalary;
    
    // Constructors
    public PayrollResponse() {
    }
    
    public PayrollResponse(UUID employeeId, String employeeName, int month, int year,
                          SalaryType salaryType, BigDecimal salaryAmount, int presentDays,
                          int approvedLeaveDays, int payableDays, BigDecimal totalSalary) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.month = month;
        this.year = year;
        this.salaryType = salaryType;
        this.salaryAmount = salaryAmount;
        this.presentDays = presentDays;
        this.approvedLeaveDays = approvedLeaveDays;
        this.payableDays = payableDays;
        this.totalSalary = totalSalary;
    }
    
    // Getters and Setters
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
    
    public int getMonth() {
        return month;
    }
    
    public void setMonth(int month) {
        this.month = month;
    }
    
    public int getYear() {
        return year;
    }
    
    public void setYear(int year) {
        this.year = year;
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
    
    public int getPresentDays() {
        return presentDays;
    }
    
    public void setPresentDays(int presentDays) {
        this.presentDays = presentDays;
    }
    
    public int getApprovedLeaveDays() {
        return approvedLeaveDays;
    }
    
    public void setApprovedLeaveDays(int approvedLeaveDays) {
        this.approvedLeaveDays = approvedLeaveDays;
    }
    
    public int getPayableDays() {
        return payableDays;
    }
    
    public void setPayableDays(int payableDays) {
        this.payableDays = payableDays;
    }
    
    public BigDecimal getTotalSalary() {
        return totalSalary;
    }
    
    public void setTotalSalary(BigDecimal totalSalary) {
        this.totalSalary = totalSalary;
    }
}
