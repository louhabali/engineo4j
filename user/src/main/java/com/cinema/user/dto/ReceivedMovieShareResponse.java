package com.cinema.user.dto;

import java.time.LocalDateTime;

public record ReceivedMovieShareResponse(
        Long id,
        Long movieId,
        Long senderUserId,
        String friendEmail,
        LocalDateTime createdAt) {
}