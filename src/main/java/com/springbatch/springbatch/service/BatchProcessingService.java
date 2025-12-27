package com.springbatch.springbatch.service;

import com.springbatch.springbatch.dto.BatchResponseDTO;
import com.springbatch.springbatch.dto.EmployeeBulkDTO;
import com.springbatch.springbatch.entity.Department;
import com.springbatch.springbatch.entity.Employee;
import com.springbatch.springbatch.entity.Project;
import com.springbatch.springbatch.repositary.DepartmentRepository;
import com.springbatch.springbatch.repositary.EmployeeRepository;
import com.springbatch.springbatch.repositary.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BatchProcessingService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public BatchResponseDTO processBulkJson(List<EmployeeBulkDTO> employeeDTOs) {
        List<Employee> employees = convertDTOsToEntities(employeeDTOs);
        return saveEmployees(employees);
    }

    private List<Employee> convertDTOsToEntities(List<EmployeeBulkDTO> dtos) {
        List<Employee> employees = new ArrayList<>();

        for (EmployeeBulkDTO dto : dtos) {
            Employee employee = new Employee();
            employee.setName(dto.getName());
            employee.setEmail(dto.getEmail());
            employee.setPosition(dto.getPosition());
            employee.setSalary(dto.getSalary());

            Department department = getOrCreateDepartment(dto.getDepartmentName());
            employee.setDepartment(department);

            if (dto.getProjects() != null) {
                dto.getProjects().forEach(projectDTO -> {
                    Project project = new Project();
                    project.setProjectName(projectDTO.getProjectName());
                    project.setDescription(projectDTO.getDescription());
                    project.setStartDate(projectDTO.getStartDate());
                    project.setEndDate(projectDTO.getEndDate());
                    employee.addProject(project);
                });
            }

            employees.add(employee);
        }

        return employees;
    }

    public BatchResponseDTO processExcelFile(MultipartFile file) throws Exception {
        List<Employee> employees = parseExcelFile(file);
        return saveEmployees(employees);

    }

    private BatchResponseDTO saveEmployees(List<Employee> employees) {
        int successCount = 0;
        int failureCount = 0;

        for (Employee employee : employees) {
            try {
                employeeRepository.save(employee);
                successCount++;
            } catch (Exception e) {
                log.error("Failed to save employee: {}", employee.getEmail(), e);
                failureCount++;
            }
        }

        return new BatchResponseDTO(
                employees.size(),
                successCount,
                failureCount,
                "Batch processing completed"
        );
    }

    private List<Employee> parseExcelFile(MultipartFile file) throws IOException {
        List<Employee> employees = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            // Skip header row
            if (rows.hasNext()) {
                rows.next();
            }

            while (rows.hasNext()) {
                Row row = rows.next();
                Employee employee = new Employee();

                employee.setName(getCellValueAsString(row.getCell(0)));
                employee.setEmail(getCellValueAsString(row.getCell(1)));
                employee.setPosition(getCellValueAsString(row.getCell(2)));
                employee.setSalary(new BigDecimal(getCellValueAsString(row.getCell(3))));

                String deptName = getCellValueAsString(row.getCell(4));
                Department department = getOrCreateDepartment(deptName);
                employee.setDepartment(department);

                employees.add(employee);
            }
        }

        return employees;
    }

    private Department getOrCreateDepartment(String name) {
        return departmentRepository.findByName(name)
                .orElseGet(() -> {
                    Department dept = new Department();
                    dept.setName(name);
                    return departmentRepository.save(dept);
                });
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }
}
