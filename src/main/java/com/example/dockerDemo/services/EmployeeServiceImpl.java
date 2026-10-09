package com.example.dockerDemo.services;

import com.example.dockerDemo.dto.EmployeeRequestDto;
import com.example.dockerDemo.dto.EmployeeResponseDto;
import com.example.dockerDemo.exceptions.EmployeeNotFoundException;
import com.example.dockerDemo.model.Employee;
import com.example.dockerDemo.repository.EmployeeRepository;
import org.springframework.stereotype.Service;


import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional(readOnly = true)
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repository;

    private static final Set<String> SORT_FIELDS =
            Set.of("id", "name", "department", "salary", "email");

    public EmployeeServiceImpl(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public EmployeeResponseDto create(EmployeeRequestDto request) {
        if (repository.existsByEmailIgnoreCase(request.email())) {
            throw new IllegalArgumentException("Email already exists");
        }

        Employee employee = new Employee(
                request.name().trim(),
                request.email().trim(),
                request.department().trim(),
                request.salary()
        );

        return EmployeeResponseDto.from(repository.save(employee));
    }

    @Override
    public Page<EmployeeResponseDto> getEmployees(
            String department, int page, int size,
            String sortBy, String direction) {

        if (page < 0) {
            throw new IllegalArgumentException(
                    "Page must be zero or greater"
            );
        }

        if (size < 1 || size > 100) {
            throw new IllegalArgumentException(
                    "Page size must be between 1 and 100"
            );
        }

        if (!SORT_FIELDS.contains(sortBy)) {
            throw new IllegalArgumentException(
                    "Unsupported sort field: " + sortBy
            );
        }

        Sort.Direction sortDirection =
                Sort.Direction.fromString(direction);

        Pageable pageable = PageRequest.of(
                page, size, Sort.by(sortDirection, sortBy)
        );

        Page<Employee> employees;

        if (department != null && !department.isBlank()) {
            employees = repository.findByDepartmentIgnoreCase(department.trim(), pageable);
        } else {
            employees = repository.findAll(pageable);
        }

        return employees.map(EmployeeResponseDto::from);
    }

    @Override
    public EmployeeResponseDto getById(Long id) {
        return EmployeeResponseDto.from(findEmployee(id));
    }

    @Override
    @Transactional
    public EmployeeResponseDto update(Long id, EmployeeRequestDto request) {
        Employee employee = findEmployee(id);

        if (repository.existsByEmailIgnoreCaseAndIdNot(
                request.email(), id)) {
            throw new IllegalArgumentException("Email already exists");
        }

        employee.setName(request.name().trim());
        employee.setEmail(request.email().trim());
        employee.setDepartment(request.department().trim());
        employee.setSalary(request.salary());

        // Managed entity is updated by JPA dirty checking.
        return EmployeeResponseDto.from(employee);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.delete(findEmployee(id));
    }

    private Employee findEmployee(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(
                        "Employee not found with id: " + id
                ));
    }
}