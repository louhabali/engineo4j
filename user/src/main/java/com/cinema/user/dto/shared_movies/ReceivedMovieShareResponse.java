package com.cinema.user.dto.shared_movies;

import java.time.LocalDateTime;

public record ReceivedMovieShareResponse(
        Long id,
        Long movieId,
        Long senderUserId,
        String friendEmail,
        LocalDateTime createdAt) {
}