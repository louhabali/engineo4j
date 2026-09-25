package com.cinema.rating.service;

import com.cinema.rating.event.MovieRatedEvent;
import com.cinema.rating.dto.RatingRequest;
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
        // Save 
        Rating rating = new Rating();
        rating.setUserId(userId);
        rating.setMovieId(request.movieId());
        rating.setScore(request.score());
        
        Rating savedRating = ratingRepository.save(rating);

        // Publish 
        MovieRatedEvent event = new MovieRatedEvent(userId, request.movieId(), request.score());
        eventProducer.sendRatingEvent(event);

        return savedRating;
    }

    public List<Rating> getRatingsByMovie(Long movieId) {
        return ratingRepository.findByMovieId(movieId);
    }
}