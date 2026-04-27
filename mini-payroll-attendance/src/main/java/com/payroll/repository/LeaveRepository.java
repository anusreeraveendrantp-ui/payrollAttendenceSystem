package com.payroll.repository;

import com.payroll.model.Employee;
import com.payroll.model.Leave;
import com.payroll.model.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, UUID> {

    List<Leave> findByEmployee(Employee employee);

    List<Leave> findByEmployeeAndStatus(Employee employee, LeaveStatus status);

    @Query("SELECT l FROM Leave l WHERE l.employee = :employee " +
           "AND l.status = :status " +
           "AND ((l.startDate <= :endDate AND l.endDate >= :startDate))")
    List<Leave> findByEmployeeAndStatusAndDateRange(
        @Param("employee") Employee employee,
        @Param("status") LeaveStatus status,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

    // sumApprovedLeaveDaysInMonth was removed — it used DATEDIFF() which is MySQL-only
    // and fails on PostgreSQL with Hibernate 6.
    // Leave day counting is done in Java inside PayrollService.countApprovedLeaveDays()
    // using findByEmployeeAndStatusAndDateRange above.
}
