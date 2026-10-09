package com.example.dockerDemo.dto;

import com.example.dockerDemo.model.Employee;
import java.math.BigDecimal;

public record EmployeeResponseDto(
        Long id,
        String name,
        String email,
        String department,
        BigDecimal salary
) {
    public static EmployeeResponseDto from(Employee e) {
        return new EmployeeResponseDto(
                e.getId(),
                e.getName(),
                e.getEmail(),
                e.getDepartment(),
                e.getSalary()
        );
    }
}