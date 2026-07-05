package com.fitness.activityservice.services;

import com.fitness.activityservice.dto.ActivityRequestDto;
import com.fitness.activityservice.dto.ActivityResponseDto;
import com.fitness.activityservice.models.Activity;
import com.fitness.activityservice.repos.ActivityRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final UserValidationService userValidationService;
    private final ActivityRepo activityRepo;
    private KafkaTemplate<String,Activity> kafkaTemplate;

    @Value("${kafka.topic.name}") // from properties file
    private String topicName;

    public ActivityResponseDto trackActivity(ActivityRequestDto request) {
       boolean isValidUser = userValidationService.validateUser(request.getUserId());
       if (!isValidUser) throw new RuntimeException("Invalid User " + request.getUserId());
        Activity activity = Activity.builder()
                .userId(request.getUserId())
                .type(request.getType())
                .duration(request.getDuration())
                .caloriesBurned(request.getCaloriesBurned())
                .startTime(request.getStartTime())
                .additionalMatrices(request.getAdditionalMatrices())
                .build();
        Activity savedActivity = activityRepo.save(activity);

        try {
            kafkaTemplate.send(topicName,savedActivity.getUserId(), savedActivity);
        } catch (Exception e){
            e.printStackTrace();
        }
        return mapToResponseDto(savedActivity);
    }

    private ActivityResponseDto mapToResponseDto(Activity activity) {
        ActivityResponseDto response = new ActivityResponseDto();
        response.setId(activity.getId());
        response.setUserId(activity.getUserId());
        response.setType(activity.getType());
        response.setDuration(activity.getDuration());
        response.setCaloriesBurned(activity.getCaloriesBurned());
        response.setStartTime(activity.getStartTime());
        response.setAdditionalMatrices(activity.getAdditionalMatrices());
        response.setCreatedAt(activity.getCreatedAt());
        response.setUpdatedAt(activity.getUpdatedAt());
        return response;
    }

    public List<ActivityResponseDto> getUserActivities(String userId) {
        return activityRepo.findByUserId(userId)
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }
}
