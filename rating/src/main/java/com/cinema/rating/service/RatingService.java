package com.cinema.rating.service;

import com.cinema.rating.event.MovieRatedEvent;
import com.cinema.rating.dto.RatingRequest;
import com.cinema.rating.dto.RatingSummaryResponse;
import com.cinema.rating.entity.Rating;
import com.cinema.rating.producer.RatingEventProducer;
import com.cinema.rating.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final RatingEventProducer eventProducer;

    @Transactional
    public Rating addRating(String userId, RatingRequest request) {
        Rating rating = ratingRepository.findByUserIdAndMovieId(userId, request.movieId())
                .orElseGet(() -> {
                    Rating newRating = new Rating();
                    newRating.setUserId(userId);
                    newRating.setMovieId(request.movieId());
                    return newRating;
                });
        rating.setScore(request.score());

        Rating savedRating = ratingRepository.save(rating);

        MovieRatedEvent event = new MovieRatedEvent(
                savedRating.getUserId(),
                savedRating.getMovieId(),
                savedRating.getScore());
        eventProducer.sendRatingEvent(event);

        return savedRating;
    }

    public List<Rating> getRatingsByMovie(Long movieId) {
        return ratingRepository.findByMovieId(movieId);
    }

    public RatingSummaryResponse getRatingSummary(Long movieId, String userId) {
        return ratingRepository.getSummaryByMovieId(movieId, userId);
    }

    public List<RatingSummaryResponse> getRatingSummaries(List<Long> movieIds) {
        if (movieIds == null || movieIds.isEmpty()) {
            return List.of();
        }
        return ratingRepository.getSummariesByMovieIds(movieIds);
    }
}
