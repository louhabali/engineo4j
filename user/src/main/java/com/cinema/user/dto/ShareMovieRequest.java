package com.cinema.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ShareMovieRequest(
        @NotNull(message = "Movie ID is required")
        @Positive(message = "Movie ID must be positive")
        Long movieId,

        @NotBlank(message = "Friend email is required")
        @Email(message = "Friend email must be valid")
        String friendEmail) {
}