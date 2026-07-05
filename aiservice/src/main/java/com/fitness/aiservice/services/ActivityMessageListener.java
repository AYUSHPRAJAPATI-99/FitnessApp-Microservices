package com.fitness.aiservice.services;

import com.fitness.aiservice.dto.RecommendationDto;
import com.fitness.aiservice.models.Activity;
import com.fitness.aiservice.models.Recommendation;
import com.fitness.aiservice.repo.RecommendationRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor     // generator constructor for final,Nonnull
public class ActivityMessageListener {
    private final AiActivityService aiActivityService;
    private  final RecommendationRepo recommendationRepo;

//    @Value("${kafka.topic.name}") // from properties file
//    private String topicName;
//    You get the error because the topics attribute of @KafkaListener is evaluated when Spring processes annotations, and Java annotations require their attribute values to be compile-time constants.
//    jo bhi aayega kafka me to activity ke taraf se ho activity me map ho jayega
    // ye msg broker nahi hai (wo to kafka hai) ye bas process kr raha (kafka se lekr(listen krke le raha)
    @KafkaListener(topics = "${kafka.topic.name}",groupId = "activity-processor-group")
    public void processActivity(Activity activity){
        log.info("Receive Activity for processing: {}", activity.getUserId());
        RecommendationDto recommendationDto = aiActivityService.generateRecommendations(activity);
        Recommendation recommendation =  Recommendation.builder().activityId(activity.getId()) // recommendationDto.getId() se lag raha tha ki
                            // recommendationDto ki khud ki id hai
                .userId(recommendationDto.getUserId())
                .type(recommendationDto.getType())
                .recommendation(recommendationDto.getRecommendation())
                .improvements(recommendationDto.getImprovements())
                .suggestions(recommendationDto.getSuggestions())
                .safety(recommendationDto.getSafety())
                .createdAt(recommendationDto.getCreatedAt())
                .build();
        recommendationRepo.save(recommendation);
    }
}
