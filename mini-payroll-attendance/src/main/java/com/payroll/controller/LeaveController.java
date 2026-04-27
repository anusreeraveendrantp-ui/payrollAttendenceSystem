package com.payroll.controller;

import com.payroll.dto.ApplyLeaveRequest;
import com.payroll.dto.LeaveResponse;
import com.payroll.exception.ForbiddenException;
import com.payroll.service.LeaveService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/leaves")
public class LeaveController {
    
    private final LeaveService leaveService;
    
    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }
    
    @PostMapping
    public ResponseEntity<LeaveResponse> applyLeave(@Valid @RequestBody ApplyLeaveRequest request) {
        LeaveResponse response = leaveService.applyLeave(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PutMapping("/{id}/approve")
    public ResponseEntity<LeaveResponse> approveLeave(@PathVariable UUID id) {
        LeaveResponse response = leaveService.approveLeave(id);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}/reject")
    public ResponseEntity<LeaveResponse> rejectLeave(@PathVariable UUID id) {
        LeaveResponse response = leaveService.rejectLeave(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    public ResponseEntity<List<LeaveResponse>> getAllLeaves() {
        List<LeaveResponse> leaves = leaveService.getAllLeaves();
        return ResponseEntity.ok(leaves);
    }
    
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<LeaveResponse>> getLeavesByEmployee(
            @PathVariable UUID employeeId,
            Authentication authentication) {
        
        // EMPLOYEE role can only access their own leaves
        if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            UUID authenticatedEmployeeId = (UUID) authentication.getPrincipal();
            if (!authenticatedEmployeeId.equals(employeeId)) {
                throw new ForbiddenException("You can only access your own leave records");
            }
        }
        
        List<LeaveResponse> leaves = leaveService.getLeavesByEmployee(employeeId);
        return ResponseEntity.ok(leaves);
    }
}
