package com.cinema.recommendation.event; // adjust package to match your project

public record MovieRatedEvent(
    String userId,
    Long movieId,
    int score
) {}