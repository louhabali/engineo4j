package com.cinema.recommendation.controller;

import com.cinema.recommendation.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping
    public ResponseEntity<Collection<Map<String, Object>>> getRecommendations(
            @RequestHeader(value = "X-User-Id", defaultValue = "mock-user-123") String userId) {
        
        Collection<Map<String, Object>> recommendations = recommendationService.getRecommendationsForUser(userId);
        return ResponseEntity.ok(recommendations);
    }
}