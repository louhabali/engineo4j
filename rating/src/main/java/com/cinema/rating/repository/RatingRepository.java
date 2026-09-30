package com.cinema.rating.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cinema.rating.entity.Rating;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByMovieId(Long movieId);
    List<Rating> findByUserId(String userId);
    Optional<Rating> findByUserIdAndMovieId(String userId, Long movieId);
}
