package com.cinema.rating.controller;

import com.cinema.rating.dto.RatingRequest;
import com.cinema.rating.dto.RatingSummaryResponse;
import com.cinema.rating.entity.Rating;
import com.cinema.rating.service.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @PostMapping
    public ResponseEntity<Rating> addRating(
            @RequestHeader(value = "X-User-Id") String userId,
            @Valid @RequestBody RatingRequest request) {

        Rating savedRating = ratingService.addRating(userId, request);
        return ResponseEntity.ok(savedRating);
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<Rating>> getByMovie(@PathVariable Long movieId) {
        List<Rating> ratings = ratingService.getRatingsByMovie(movieId);
        return ResponseEntity.ok(ratings);
    }

    @GetMapping("/movie/{movieId}/summary")
    public ResponseEntity<RatingSummaryResponse> getSummary(@PathVariable Long movieId,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        return ResponseEntity.ok(ratingService.getRatingSummary(movieId, userId));
    }

    @GetMapping("/summaries")
    public ResponseEntity<List<RatingSummaryResponse>> getSummaries(
            @RequestParam List<Long> movieIds) {
        return ResponseEntity.ok(ratingService.getRatingSummaries(movieIds));
    }
}
