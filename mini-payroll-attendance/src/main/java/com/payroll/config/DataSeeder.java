package com.payroll.config;

import com.payroll.model.Employee;
import com.payroll.model.EmployeeRole;
import com.payroll.model.SalaryType;
import com.payroll.model.SystemRole;
import com.payroll.repository.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

@Configuration
public class DataSeeder {
    
    @Bean
    public CommandLineRunner seedAdminUser(EmployeeRepository employeeRepository, 
                                          PasswordEncoder passwordEncoder) {
        return args -> {
            if (!employeeRepository.existsBySystemRole(SystemRole.ADMIN)) {
                Employee admin = new Employee(
                    "Admin User",
                    EmployeeRole.OFFICE,
                    SalaryType.MONTHLY,
                    new BigDecimal("50000"),
                    SystemRole.ADMIN,
                    "admin",
                    passwordEncoder.encode("admin123")
                );
                employeeRepository.save(admin);
                System.out.println("Admin user created: username=admin, password=admin123");
            }
        };
    }
}
