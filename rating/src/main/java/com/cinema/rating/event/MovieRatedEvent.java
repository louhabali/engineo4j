package com.cinema.rating.event; // adjust package to match your project

public record MovieRatedEvent(
    String userId,
    Long movieId,
    int score
) {}