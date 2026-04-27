package com.payroll.service;

import com.payroll.dto.LoginRequest;
import com.payroll.dto.LoginResponse;
import com.payroll.exception.UnauthorizedException;
import com.payroll.model.Employee;
import com.payroll.repository.EmployeeRepository;
import com.payroll.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    
    public AuthService(EmployeeRepository employeeRepository, 
                      PasswordEncoder passwordEncoder,
                      JwtUtil jwtUtil) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }
    
    public LoginResponse login(LoginRequest request) {
        Employee employee = employeeRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));
        
        if (!passwordEncoder.matches(request.getPassword(), employee.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }
        
        String token = jwtUtil.generateToken(employee.getId(), employee.getSystemRole());
        
        return new LoginResponse(token, employee.getId(), employee.getSystemRole());
    }
}
