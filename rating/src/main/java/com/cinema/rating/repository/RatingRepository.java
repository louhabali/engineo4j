package com.cinema.rating.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cinema.rating.entity.Rating;
import com.cinema.rating.dto.RatingSummaryResponse;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByMovieId(Long movieId);

    List<Rating> findByUserId(String userId);

    Optional<Rating> findByUserIdAndMovieId(String userId, Long movieId);

    @Query("""
                SELECT new com.cinema.rating.dto.RatingSummaryResponse(
                    :movieId,
                    COUNT(r),
                    COALESCE(AVG(r.score), 0.0),
                    MAX(CASE WHEN r.userId = :userId THEN r.score ELSE NULL END)
                )
                FROM Rating r
                WHERE r.movieId = :movieId
            """)
    RatingSummaryResponse getSummaryByMovieId(
            @Param("movieId") Long movieId,
            @Param("userId") String userId);

    @Query("SELECT new com.cinema.rating.dto.RatingSummaryResponse(r.movieId, COUNT(r), AVG(r.score), 0) " +
            "FROM Rating r WHERE r.movieId IN :movieIds GROUP BY r.movieId")
    List<RatingSummaryResponse> getSummariesByMovieIds(@Param("movieIds") List<Long> movieIds);
}
