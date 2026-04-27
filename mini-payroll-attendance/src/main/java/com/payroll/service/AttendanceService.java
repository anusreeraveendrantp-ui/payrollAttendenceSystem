package com.payroll.service;

import com.payroll.dto.AttendanceResponse;
import com.payroll.dto.MarkAttendanceRequest;
import com.payroll.exception.DuplicateResourceException;
import com.payroll.exception.ResourceNotFoundException;
import com.payroll.model.Attendance;
import com.payroll.model.Employee;
import com.payroll.repository.AttendanceRepository;
import com.payroll.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AttendanceService {
    
    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;
    
    public AttendanceService(AttendanceRepository attendanceRepository, 
                            EmployeeRepository employeeRepository) {
        this.attendanceRepository = attendanceRepository;
        this.employeeRepository = employeeRepository;
    }
    
    @Transactional
    public AttendanceResponse markAttendance(MarkAttendanceRequest request) {
        // Check if employee exists
        Employee employee = employeeRepository.findById(request.getEmployeeId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Employee not found with id: " + request.getEmployeeId()));
        
        // Check for duplicate attendance record
        if (attendanceRepository.existsByEmployeeAndDate(employee, request.getDate())) {
            throw new DuplicateResourceException(
                "Attendance already marked for employee " + request.getEmployeeId() + 
                " on date " + request.getDate());
        }
        
        // Create and save attendance record
        Attendance attendance = new Attendance(employee, request.getDate(), request.getStatus());
        Attendance savedAttendance = attendanceRepository.save(attendance);
        
        return AttendanceResponse.fromEntity(savedAttendance);
    }
    
    @Transactional(readOnly = true)
    public List<AttendanceResponse> getAttendanceByEmployee(UUID employeeId) {
        // Check if employee exists
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Employee not found with id: " + employeeId));
        
        // Get all attendance records for the employee
        return attendanceRepository.findByEmployee(employee)
            .stream()
            .map(AttendanceResponse::fromEntity)
            .collect(Collectors.toList());
    }
}
