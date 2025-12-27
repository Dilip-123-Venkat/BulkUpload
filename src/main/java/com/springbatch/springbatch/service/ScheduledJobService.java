package com.springbatch.springbatch.service;

import com.springbatch.springbatch.entity.Department;
import com.springbatch.springbatch.entity.Employee;
import com.springbatch.springbatch.repositary.DepartmentRepository;
import com.springbatch.springbatch.repositary.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduledJobService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    // Runs every 5 minutes

    @Scheduled(cron = "0 */1 * * * *")
    @Transactional
    public void processEmployeeDataEvery5Minutes() {
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        log.info("==============================================");
        log.info("Scheduled Job Started at: {}", currentTime);
        log.info("==============================================");

        try {
            // Example: Get all employees and process them
            List<Employee> employees = employeeRepository.findAll();
            log.info("Total Employees found: {}", employees.size());

            // Example: Get all departments
            List<Department> departments = departmentRepository.findAll();
            log.info("Total Departments found: {}", departments.size());

            // Your custom business logic here
            // For example: Update employee status, send notifications, etc.

            for (Employee emp : employees) {
                log.info("Processing Employee: {} - {}", emp.getName(), emp.getEmail());
                // Add your logic here
            }

            log.info("Scheduled Job Completed Successfully at: {}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        } catch (Exception e) {
            log.error("Error in scheduled job: ", e);
        }

        log.info("==============================================");
    }

    // Alternative: Runs every 5 minutes using fixedDelay
    // @Scheduled(fixedDelay = 300000) // 5 minutes in milliseconds
    public void alternativeScheduledJob() {
        log.info("Alternative scheduled job executed");
    }

    // Example: Run every hour
    // @Scheduled(cron = "0 0 * * * *")
    public void hourlyJob() {
        log.info("Hourly job executed");
    }

    // Example: Run daily at 2 AM
    // @Scheduled(cron = "0 0 2 * * *")
    public void dailyJob() {
        log.info("Daily job executed at 2 AM");
    }
}