package com.springbatch.springbatch.controller;

import com.springbatch.springbatch.dto.BatchResponseDTO;
import com.springbatch.springbatch.dto.EmployeeBulkDTO;
import com.springbatch.springbatch.service.BatchProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/batch")
@RequiredArgsConstructor
public class BatchController {
    private final BatchProcessingService batchProcessingService;

    @PostMapping("/upload-excel")
    public ResponseEntity<BatchResponseDTO> uploadExcel(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(new BatchResponseDTO(0, 0, 0, "File is empty"));
        }

        try {
            BatchResponseDTO response = batchProcessingService.processExcelFile(file);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BatchResponseDTO(0, 0, 0, "Error Processing FIle " + e.getMessage()));
        }
    }

    @PostMapping("/bulk-json")
    public ResponseEntity<BatchResponseDTO> uploadBulkJson(@RequestBody List<EmployeeBulkDTO> employeeDTOs) {
        if (employeeDTOs == null || employeeDTOs.isEmpty()) {
            return ResponseEntity.badRequest().body(new BatchResponseDTO(0, 0, 0, "No Data Provided"));

        }

        BatchResponseDTO response = batchProcessingService.processBulkJson(employeeDTOs);
        return ResponseEntity.ok(response);


    }
}
