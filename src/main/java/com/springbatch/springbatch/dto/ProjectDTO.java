package com.springbatch.springbatch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {
    private String projectName;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
}