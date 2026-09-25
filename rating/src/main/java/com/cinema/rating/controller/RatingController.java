package com.cinema.rating.controller;

import com.cinema.rating.dto.RatingRequest;
import com.cinema.rating.entity.Rating;
import com.cinema.rating.service.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @PostMapping
    public ResponseEntity<Rating> addRating(
            @RequestHeader(value = "X-User-Id", defaultValue = "mock-user-123") String userId,
            @Valid @RequestBody RatingRequest request) {
        
        Rating savedRating = ratingService.addRating(userId, request);
        return ResponseEntity.ok(savedRating);
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<Rating>> getByMovie(@PathVariable Long movieId) {
        List<Rating> ratings = ratingService.getRatingsByMovie(movieId);
        return ResponseEntity.ok(ratings);
    }
}