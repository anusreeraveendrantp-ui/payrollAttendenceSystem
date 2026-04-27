package com.payroll.repository;

import com.payroll.model.Employee;
import com.payroll.model.SystemRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    
    Optional<Employee> findByUsername(String username);
    
    boolean existsByUsername(String username);
    
    boolean existsBySystemRole(SystemRole systemRole);
}
