package com.cinema.movie.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "movies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String movieId;

    @Column(nullable = false)
    private String title;

    private String tagline;

    @Column(length = 1000)
    private String description; 

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "movie_genres", joinColumns = @JoinColumn(name = "movie_id"))
    @Column(name = "genre")
    private List<String> genres;

    private Integer releaseYear;
    private Double averageRating;
    
    private String posterUrl;
    private String bannerUrl;
    private String duration; // e.g., "2h 22m"
    private String director;
}