package com.cinema.movie.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieRequestDto {

    @NotBlank(message = "Movie ID is required")
    private String movieId;

    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Genres list cannot be null")
    private List<String> genres;

    @NotNull(message = "Release year is required")
    private Integer releaseYear;
}