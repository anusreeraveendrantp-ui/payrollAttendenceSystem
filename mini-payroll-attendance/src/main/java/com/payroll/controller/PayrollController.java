package com.payroll.controller;

import com.payroll.dto.PayrollResponse;
import com.payroll.exception.ForbiddenException;
import com.payroll.service.PayrollService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/payroll")
public class PayrollController {
    
    private final PayrollService payrollService;
    
    public PayrollController(PayrollService payrollService) {
        this.payrollService = payrollService;
    }
    
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<PayrollResponse> getPayroll(
            @PathVariable UUID employeeId,
            @RequestParam int month,
            @RequestParam int year,
            Authentication authentication) {
        
        // EMPLOYEE role can only access their own payroll
        if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            UUID authenticatedEmployeeId = (UUID) authentication.getPrincipal();
            if (!authenticatedEmployeeId.equals(employeeId)) {
                throw new ForbiddenException("You can only access your own payroll data");
            }
        }
        
        PayrollResponse response = payrollService.calculatePayroll(employeeId, month, year);
        return ResponseEntity.ok(response);
    }
}
