package com.springbatch.springbatch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchResponseDTO {
    private int totalRecords;
    private int successCount;
    private int failureCount;
    private String message;
}
