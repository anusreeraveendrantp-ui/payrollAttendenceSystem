package com.payroll.service;

import com.payroll.dto.PayrollResponse;
import com.payroll.exception.ResourceNotFoundException;
import com.payroll.model.AttendanceStatus;
import com.payroll.model.Employee;
import com.payroll.model.Leave;
import com.payroll.model.LeaveStatus;
import com.payroll.model.SalaryType;
import com.payroll.repository.AttendanceRepository;
import com.payroll.repository.EmployeeRepository;
import com.payroll.repository.LeaveRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

@Service
public class PayrollService {
    
    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;
    private final LeaveRepository leaveRepository;
    
    public PayrollService(EmployeeRepository employeeRepository,
                         AttendanceRepository attendanceRepository,
                         LeaveRepository leaveRepository) {
        this.employeeRepository = employeeRepository;
        this.attendanceRepository = attendanceRepository;
        this.leaveRepository = leaveRepository;
    }
    
    @Transactional(readOnly = true)
    public PayrollResponse calculatePayroll(UUID employeeId, int month, int year) {
        // Load employee
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));
        
        // Calculate date range for the month
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate monthStart = yearMonth.atDay(1);
        LocalDate monthEnd = yearMonth.atEndOfMonth();
        
        // Count present days
        long presentDays = attendanceRepository.countByEmployeeAndDateBetweenAndStatus(
            employee, monthStart, monthEnd, AttendanceStatus.PRESENT
        );
        
        // Count approved leave days
        int approvedLeaveDays = countApprovedLeaveDays(employee, monthStart, monthEnd);
        
        // Compute payable days
        int payableDays = (int) presentDays + approvedLeaveDays;
        
        // Apply salary formula
        BigDecimal totalSalary = calculateTotalSalary(
            employee.getSalaryType(), 
            employee.getSalaryAmount(), 
            payableDays
        );
        
        // Return DTO
        return new PayrollResponse(
            employee.getId(),
            employee.getName(),
            month,
            year,
            employee.getSalaryType(),
            employee.getSalaryAmount(),
            (int) presentDays,
            approvedLeaveDays,
            payableDays,
            totalSalary
        );
    }
    
    private int countApprovedLeaveDays(Employee employee, LocalDate monthStart, LocalDate monthEnd) {
        List<Leave> approvedLeaves = leaveRepository.findByEmployeeAndStatusAndDateRange(
            employee, LeaveStatus.APPROVED, monthStart, monthEnd
        );
        
        int totalDays = 0;
        for (Leave leave : approvedLeaves) {
            // Calculate overlap between leave period and month period
            LocalDate overlapStart = leave.getStartDate().isBefore(monthStart) ? monthStart : leave.getStartDate();
            LocalDate overlapEnd = leave.getEndDate().isAfter(monthEnd) ? monthEnd : leave.getEndDate();
            
            // Count days in overlap (inclusive)
            long days = overlapEnd.toEpochDay() - overlapStart.toEpochDay() + 1;
            totalDays += (int) days;
        }
        
        return totalDays;
    }
    
    private BigDecimal calculateTotalSalary(SalaryType salaryType, BigDecimal salaryAmount, int payableDays) {
        if (salaryType == SalaryType.MONTHLY) {
            // Formula: (salaryAmount / 30) * payableDays
            return salaryAmount
                .divide(BigDecimal.valueOf(30), 10, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(payableDays))
                .setScale(2, RoundingMode.HALF_UP);
        } else {
            // DAILY: salaryAmount * payableDays
            return salaryAmount
                .multiply(BigDecimal.valueOf(payableDays))
                .setScale(2, RoundingMode.HALF_UP);
        }
    }
}
