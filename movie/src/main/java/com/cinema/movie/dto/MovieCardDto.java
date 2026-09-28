package com.cinema.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieCardDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String movieId;
    private String title;
    private Integer releaseYear;
    private Double averageRating;
    private List<String> genres;
    private String posterUrl;
    private String bannerUrl;
}