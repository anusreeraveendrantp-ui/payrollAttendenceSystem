package com.payroll.repository;

import com.payroll.model.Attendance;
import com.payroll.model.AttendanceStatus;
import com.payroll.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, UUID> {
    
    Optional<Attendance> findByEmployeeAndDate(Employee employee, LocalDate date);
    
    boolean existsByEmployeeAndDate(Employee employee, LocalDate date);
    
    List<Attendance> findByEmployee(Employee employee);
    
    List<Attendance> findByEmployeeAndDateBetweenAndStatus(
        Employee employee, 
        LocalDate startDate, 
        LocalDate endDate, 
        AttendanceStatus status
    );
    
    long countByEmployeeAndDateBetweenAndStatus(
        Employee employee, 
        LocalDate startDate, 
        LocalDate endDate, 
        AttendanceStatus status
    );
}
