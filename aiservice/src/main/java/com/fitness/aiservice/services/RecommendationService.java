package com.fitness.aiservice.services;

import com.fitness.aiservice.dto.RecommendationDto;
import com.fitness.aiservice.models.Recommendation;
import com.fitness.aiservice.repo.RecommendationRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final RecommendationRepo recommendationRepo;

    public List<RecommendationDto> getUserRecommendation(String userId) {
        List<Recommendation> entityList = recommendationRepo.findByUserId(userId);
        return entityList.stream()
                .map(this::convertToDto) // Helper method call ho raha hai yahan
                .collect(Collectors.toList());
    }

    public RecommendationDto getActivityRecommendation(String activityId) {
        Recommendation recommendation= recommendationRepo.findByActivityId(activityId).orElseThrow(()-> new RuntimeException("No Recommendation find for this activity:"+ activityId ));
        return convertToDto(recommendation);
    }


    private RecommendationDto convertToDto(Recommendation entity) {
        if (entity == null) {
            return null;
        }

        RecommendationDto dto = new RecommendationDto();
        dto.setId(entity.getId());
        dto.setActivityId(entity.getActivityId());
        dto.setUserId(entity.getUserId());
        dto.setRecommendation(entity.getRecommendation());

        // Lists ko safe trike se copy karna takti NullPointerException na aaye
        dto.setImprovements(entity.getImprovements() != null ? List.copyOf(entity.getImprovements()) : List.of());
        dto.setSuggestions(entity.getSuggestions() != null ? List.copyOf(entity.getSuggestions()) : List.of());
        dto.setSafety(entity.getSafety() != null ? List.copyOf(entity.getSafety()) : List.of());

        dto.setCreatedAt(entity.getCreatedAt());

        return dto;
    }
}
