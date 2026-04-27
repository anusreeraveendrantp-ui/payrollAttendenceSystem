package com.payroll.service;

import com.payroll.dto.ApplyLeaveRequest;
import com.payroll.dto.LeaveResponse;
import com.payroll.exception.BadRequestException;
import com.payroll.exception.ResourceNotFoundException;
import com.payroll.model.Employee;
import com.payroll.model.Leave;
import com.payroll.model.LeaveStatus;
import com.payroll.repository.EmployeeRepository;
import com.payroll.repository.LeaveRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LeaveService {
    
    private final LeaveRepository leaveRepository;
    private final EmployeeRepository employeeRepository;
    
    public LeaveService(LeaveRepository leaveRepository, EmployeeRepository employeeRepository) {
        this.leaveRepository = leaveRepository;
        this.employeeRepository = employeeRepository;
    }
    
    @Transactional
    public LeaveResponse applyLeave(ApplyLeaveRequest request) {
        // Validate date range
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new BadRequestException("startDate must not be after endDate");
        }
        
        // Check if employee exists
        Employee employee = employeeRepository.findById(request.getEmployeeId())
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        
        // Create leave with PENDING status
        Leave leave = new Leave(
            employee,
            request.getStartDate(),
            request.getEndDate(),
            LeaveStatus.PENDING,
            request.getReason()
        );
        
        Leave savedLeave = leaveRepository.save(leave);
        return LeaveResponse.fromEntity(savedLeave);
    }
    
    @Transactional
    public LeaveResponse approveLeave(UUID leaveId) {
        Leave leave = leaveRepository.findById(leaveId)
            .orElseThrow(() -> new ResourceNotFoundException("Leave not found"));
        
        // Only PENDING leaves can be approved
        if (leave.getStatus() != LeaveStatus.PENDING) {
            throw new BadRequestException("Only PENDING leave requests can be approved");
        }
        
        leave.setStatus(LeaveStatus.APPROVED);
        Leave updatedLeave = leaveRepository.save(leave);
        return LeaveResponse.fromEntity(updatedLeave);
    }
    
    @Transactional
    public LeaveResponse rejectLeave(UUID leaveId) {
        Leave leave = leaveRepository.findById(leaveId)
            .orElseThrow(() -> new ResourceNotFoundException("Leave not found"));
        
        // Only PENDING leaves can be rejected
        if (leave.getStatus() != LeaveStatus.PENDING) {
            throw new BadRequestException("Only PENDING leave requests can be rejected");
        }
        
        leave.setStatus(LeaveStatus.REJECTED);
        Leave updatedLeave = leaveRepository.save(leave);
        return LeaveResponse.fromEntity(updatedLeave);
    }
    
    public List<LeaveResponse> getAllLeaves() {
        return leaveRepository.findAll().stream()
            .map(LeaveResponse::fromEntity)
            .collect(Collectors.toList());
    }
    
    public List<LeaveResponse> getLeavesByEmployee(UUID employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        
        return leaveRepository.findByEmployee(employee).stream()
            .map(LeaveResponse::fromEntity)
            .collect(Collectors.toList());
    }
}
