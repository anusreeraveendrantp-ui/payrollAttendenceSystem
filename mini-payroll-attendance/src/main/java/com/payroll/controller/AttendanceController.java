package com.payroll.controller;

import com.payroll.dto.AttendanceResponse;
import com.payroll.dto.MarkAttendanceRequest;
import com.payroll.exception.ForbiddenException;
import com.payroll.service.AttendanceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {
    
    private final AttendanceService attendanceService;
    
    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }
    
    @PostMapping
    public ResponseEntity<AttendanceResponse> markAttendance(@Valid @RequestBody MarkAttendanceRequest request) {
        AttendanceResponse response = attendanceService.markAttendance(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<AttendanceResponse>> getAttendanceByEmployee(
            @PathVariable UUID employeeId,
            Authentication authentication) {
        
        // EMPLOYEE role can only access their own attendance
        if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            UUID authenticatedEmployeeId = (UUID) authentication.getPrincipal();
            if (!authenticatedEmployeeId.equals(employeeId)) {
                throw new ForbiddenException("You can only access your own attendance records");
            }
        }
        
        List<AttendanceResponse> attendanceList = attendanceService.getAttendanceByEmployee(employeeId);
        return ResponseEntity.ok(attendanceList);
    }
}
