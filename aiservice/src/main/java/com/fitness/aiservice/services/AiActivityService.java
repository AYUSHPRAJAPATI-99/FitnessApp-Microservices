package com.fitness.aiservice.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.aiservice.dto.RecommendationDto;
import com.fitness.aiservice.models.Activity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AiActivityService { //ai response process here
    private final GeminiService geminiService;

    public RecommendationDto generateRecommendations(Activity activity){
        String prompt = createPromptForActivity(activity);
        String aiResponse =geminiService.getRecommendations(prompt);
        log.info("RESPONSE FROM AI {}", aiResponse);
        return processAIResponse(activity,aiResponse);
    }

    private RecommendationDto processAIResponse(Activity activity, String aiResponse) {
        try {
            ObjectMapper mapper = new ObjectMapper(); // to work with java obj and Json
            JsonNode rootNode =  mapper.readTree(aiResponse);     // aiResponse -> string
            JsonNode textNode = rootNode.path("candidates").get(0) // first ele
                    .path("content")
                    .get("parts")       // path(null safer),get same key ko access ke liye
                    .get(0)
                    .path("text");
            String jsonContent = textNode.asText().replaceAll("json\\n","")
                    .replaceAll("\\n","")
                    .trim();
            log.info("Response from cleaned ai text", jsonContent);
            JsonNode analysisJson = mapper.readTree(jsonContent);
            JsonNode analysisNode  = analysisJson.path("analysis");
            StringBuilder fullAnalysis = new StringBuilder();
            addAnalysisSection(fullAnalysis,analysisNode,"overall","Overall:");
            addAnalysisSection(fullAnalysis,analysisNode,"pace","Pace:");
            addAnalysisSection(fullAnalysis,analysisNode,"heartRate","Heart Rate:");
            addAnalysisSection(fullAnalysis,analysisNode,"calories Burned","Calories:");

            List<String> improvements = extractImprovements(analysisJson.path("improvements"));
            List<String> suggestions = extractSuggestions(analysisJson.path("suggestions"));
            List<String> safety = extractSafetyGuidelines(analysisJson.path("safety"));
            return RecommendationDto.builder().activityId(activity.getId())
                    .userId(activity.getUserId())
                    .type(activity.getType().toString())
                    .recommendation(fullAnalysis.toString().trim())
                    .improvements(improvements)
                    .suggestions(suggestions)
                    .safety(safety)
                    .createdAt(LocalDateTime.now())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return createDefaultRecommendation(activity);
        }

    }

    private RecommendationDto createDefaultRecommendation(Activity activity) {
        return RecommendationDto.builder().activityId(activity.getId())
                .userId(activity.getUserId())
                .type(activity.getType().toString())
                .recommendation("Unable to generate detailed analysis")
                .improvements(Collections.singletonList("Continue with your current routine"))
                .suggestions(Collections.singletonList("Consider consulting a fitness consultant"))
                .safety(Arrays.asList(
                        "Always warm up before exercise",
                        "Stay hydrated",
                        "Listen to your body"
                ))
                .createdAt(LocalDateTime.now())
                .build();
    }

    private List<String> extractSuggestions(JsonNode suggestionsNode) {
        List<String> suggestions = new ArrayList<>();
        if (suggestionsNode.isArray()){
            suggestionsNode.forEach(suggestion -> {
                String workout =suggestion.path("workout").asText();
                String description =suggestion.path("description").asText();
                suggestions.add(String.format("%s: %s",workout, description));
            });
        }
        return suggestions.isEmpty()? Collections.singletonList("No specific suggestions provided"): suggestions;
    }


    private List<String> extractSafetyGuidelines(JsonNode safetyNode) {
        List<String> safety = new ArrayList<>();
        if (safetyNode.isArray()){
            safetyNode.forEach(item -> safety.add(item.asText()));
        }
        return safety.isEmpty()? Collections.singletonList("Follow general safety guidelines"): safety;

    }

    private List<String> extractImprovements(JsonNode improvementsNode) {
        List<String> improvements = new ArrayList<>();
        if (improvementsNode.isArray()){
            improvementsNode.forEach(improvement -> {
                String area = improvement.path("area").asText();
                String details = improvement.path("recommendation").asText();
                improvements.add(String.format("%s: %s",area, details));
            });
        }
        return improvements.isEmpty()? Collections.singletonList("No specific improvements provided"): improvements;
    }

    // "Overall" : "This was an excellent"
    // Overall : This was an excellent
    private void addAnalysisSection(StringBuilder fullAnalysis, JsonNode analysisNode, String key, String prefix) {
        if (!analysisNode.path(key).isMissingNode()){
            fullAnalysis.append(prefix)
                    .append(analysisNode.path(key).asText())
                    .append("\n\n");
        }
    }

    private String createPromptForActivity(Activity activity) {
        return String.format("""
                Analyze this fitness activity and provide detailed recommendations in the following EXACT JSON format:
                {
                    "analysis": {
                        "overall": "Overall analysis here",
                        "pace": "Pace analysis here",
                        "heartRate": "Heart rate analysis here",
                        "calories Burned": "Calories analysis here"
                    },
                    "improvements": [
                        {
                            "area": "Area name"
                            "recommendation": "Detailed recommendation"
                        }
                    ],
                    "suggestions": [
                        {
                            "workout": "Workout name",
                            "description": "Detailed workout description"
                        }
                    ],
                    "safety": [
                        "Safety point 1",
                        "Safety point 2"
                        ]
                }
                
                Analyze this activity:
                Activity Type: %s
                Duration: %d minutes
                Calories Burned: %d
                Additional Metrics: %s
                
                Provide detailed analysis focusing on performance, improvements, next workout suggestions and safety guidelines.
                Ensure the response follows the EXACT JSON format shown above.
                """, activity.getType(),activity.getDuration(),activity.getCaloriesBurned(),activity.getAdditionalMatrices());
        // gemini response format change nahi hua hai but to output (text)[context] de raha uske change iss format me bola hai
    }
}

