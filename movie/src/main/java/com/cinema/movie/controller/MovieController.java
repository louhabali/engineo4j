package com.cinema.movie.controller;

import com.cinema.movie.dto.MovieResponseDto;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cinema.movie.service.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/movies")
@CrossOrigin(origins = "http://localhost:4200")
public class MovieController {

    private final MovieService movieService;
  

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
      
    }

    @GetMapping
    public ResponseEntity<List<MovieResponseDto>> getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @GetMapping("/paginated")
    public ResponseEntity<List<MovieResponseDto>> getPaginatedMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {

        return ResponseEntity.ok(movieService.getPaginatedMovies(page, size));
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<MovieResponseDto> getMovieById(@PathVariable String movieId) {
        // convert parameter to int
        int id = Integer.parseInt(movieId);
        return ResponseEntity.ok(movieService.getMovieById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<MovieResponseDto>> searchMovies(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Integer releaseYear,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {

        List<MovieResponseDto> movies = movieService.searchMovies(query, genre, releaseYear, page, size);
        return ResponseEntity.ok(movies);
    }
}