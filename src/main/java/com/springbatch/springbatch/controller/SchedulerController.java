package com.springbatch.springbatch.controller;

import com.springbatch.springbatch.service.ScheduledJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/scheduler")
@RequiredArgsConstructor
public class SchedulerController {
    private final ScheduledJobService scheduledJobService;


    /**
     * Manually trigger the scheduled job without waiting for cron
     */
    @PostMapping("/trigger-now")
    public ResponseEntity<Map<String, String>> triggerJobManually() {
        scheduledJobService.processEmployeeDataEvery5Minutes();

        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Scheduled job executed manually");
        response.put("timestamp", LocalDateTime.now().toString());

        return ResponseEntity.ok(response);
    }
    /**
     * Get scheduler status
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, String>> getSchedulerStatus() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "active");
        response.put("schedule", "Every 5 minutes (0 */5 * * * *)");
        response.put("currentTime", LocalDateTime.now().toString());
        response.put("description", "Scheduler is running and will execute automatically");

        return ResponseEntity.ok(response);
    }

}