package com.fitness.activityservice.controllers;
import com.fitness.activityservice.dto.ActivityRequestDto;
import com.fitness.activityservice.dto.ActivityResponseDto;
import com.fitness.activityservice.services.ActivityService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
@AllArgsConstructor           // for DI without autowried;
public class ActivityController {
    private ActivityService activityService;

    @GetMapping()
    public ResponseEntity<List<ActivityResponseDto>> getUserActivities(@RequestHeader("X-User-ID") String userId) {

        return ResponseEntity.ok(activityService.getUserActivities(userId));
    }
    @PostMapping() // same route pr diff-2 method work
    public ResponseEntity<ActivityResponseDto> trackActivity(@RequestBody ActivityRequestDto request, @RequestHeader("X-User-ID") String userId) {
        request.setUserId(userId);
        return ResponseEntity.ok(activityService.trackActivity(request));
    }
}
