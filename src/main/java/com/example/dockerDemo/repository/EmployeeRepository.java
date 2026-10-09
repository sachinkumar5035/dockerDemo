package com.example.dockerDemo.repository;

import com.example.dockerDemo.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {

    Page<Employee> findByDepartmentIgnoreCase(
            String department, Pageable pageable
    );

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(
            String email, Long id
    );
}
