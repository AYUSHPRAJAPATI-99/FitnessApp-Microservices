package com.fitness.aiservice.services;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class GeminiService {
    private final WebClient webClient;
    @Value("${gemini.api.url")
    private String geminiApiUrl;
    @Value("${gemini.api.key")
    private String geminiApiKey;

    public GeminiService(WebClient.Builder webClientBuilder){ //constructor to initalise webclient
        this.webClient = webClientBuilder.build();
    }

    public String getRecommendations(String details){
        Map<String, Object> requestBody = Map.of("contents", new Object[] {    // format request boy (for request to model)
                Map.of("parts", new Object[] {     // object array
                        Map.of("text", details) // details-> prompt
                })
        });
        // response ayega
        String response = webClient.post()
                .uri(geminiApiUrl)                       // ye sab postman se content type....
                .header("Content-Type","application/json")
                .header("x-goog-api-key",geminiApiKey)    // insentivity x or X
                .bodyValue(requestBody)
                .retrieve() // response retrieve
                .bodyToMono(String.class)
                .block(); //   Wait karo jab tak response aa na jaye.
//        Aur final String return karo.
        return response;
    }
}
