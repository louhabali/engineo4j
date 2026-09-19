package com.cinema.movie.dto;

import lombok.*;

import java.util.List;
import java.io.Serializable;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieResponseDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String movieId;
    private String title;
    private String tagline;
    private String description;
    private List<String> genres;
    private Integer releaseYear;
    private Double averageRating;
    private String posterUrl;
    private String bannerUrl;
    private String duration;
    private String director;
}