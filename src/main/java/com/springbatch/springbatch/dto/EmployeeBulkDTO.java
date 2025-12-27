package com.springbatch.springbatch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeBulkDTO {
    private String name;
    private String email;
    private String position;
    private BigDecimal salary;
    private String departmentName;
    private List<ProjectDTO> projects;
}
