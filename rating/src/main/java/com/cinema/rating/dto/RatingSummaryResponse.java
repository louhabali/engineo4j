package com.cinema.rating.dto;

public record RatingSummaryResponse(
    Long movieId,
    Long ratingCount,
    Double averageRating,
    Integer userRating
){
}