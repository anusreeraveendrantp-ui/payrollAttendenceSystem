package com.payroll.service;

import com.payroll.dto.CreateEmployeeRequest;
import com.payroll.dto.EmployeeResponse;
import com.payroll.exception.DuplicateResourceException;
import com.payroll.model.Employee;
import com.payroll.repository.EmployeeRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    
    private final EmployeeRepository employeeRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
    
    @Transactional
    public EmployeeResponse createEmployee(CreateEmployeeRequest request) {
        // Validate unique username
        if (employeeRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username already exists: " + request.getUsername());
        }
        
        // Hash password
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        
        // Create employee entity
        Employee employee = new Employee(
            request.getName(),
            request.getRole(),
            request.getSalaryType(),
            request.getSalaryAmount(),
            request.getSystemRole(),
            request.getUsername(),
            hashedPassword
        );
        
        // Save and return response without password
        Employee savedEmployee = employeeRepository.save(employee);
        return EmployeeResponse.fromEntity(savedEmployee);
    }
    
    @Transactional(readOnly = true)
    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll()
            .stream()
            .map(EmployeeResponse::fromEntity)
            .collect(Collectors.toList());
    }
}
