package com.cinema.rating.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cinema.rating.entity.Rating;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByMovieId(Long movieId);
    List<Rating> findByUserId(String userId);
}       