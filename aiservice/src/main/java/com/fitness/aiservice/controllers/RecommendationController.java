package com.fitness.aiservice.controllers;


import com.fitness.aiservice.dto.RecommendationDto;
import com.fitness.aiservice.services.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommendations")
public class RecommendationController {
    private final RecommendationService recommendationService;

    @GetMapping("/user/{userId}")
    // user ke liye recommendation jisme multiple activity ho sakti
    public ResponseEntity<List<RecommendationDto>> getUserRecommendation(@PathVariable String userId){
        return ResponseEntity.ok(recommendationService.getUserRecommendation(userId));
    }

    // kisi activity ke liye recommendation
    @GetMapping("/activity/{activityId}")
    public ResponseEntity<RecommendationDto> getActivityRecommendation(@PathVariable String activityId){
        return ResponseEntity.ok(recommendationService.getActivityRecommendation(activityId));

    }
}
