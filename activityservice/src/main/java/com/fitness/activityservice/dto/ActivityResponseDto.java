package com.fitness.activityservice.dto;


import com.fitness.activityservice.models.ActivityType;
import lombok.Data;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ActivityResponseDto {
    private String id;
    private String userId;
    private ActivityType type;
    private Integer duration;
    private Integer caloriesBurned;
    private LocalDateTime startTime;
    private Map<String,Object> additionalMatrices;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
