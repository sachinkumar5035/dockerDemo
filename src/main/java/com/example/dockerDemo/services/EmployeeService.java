package com.example.dockerDemo.services;

import com.example.dockerDemo.dto.EmployeeRequestDto;
import com.example.dockerDemo.dto.EmployeeResponseDto;
import org.springframework.data.domain.Page;


public interface EmployeeService {

    EmployeeResponseDto create(EmployeeRequestDto request);

    Page<EmployeeResponseDto> getEmployees(
            String department, int page, int size,
            String sortBy, String direction
    );

    EmployeeResponseDto getById(Long id);

    EmployeeResponseDto update(Long id, EmployeeRequestDto request);

    void delete(Long id);
}