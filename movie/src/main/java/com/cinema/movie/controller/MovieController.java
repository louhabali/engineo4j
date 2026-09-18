package com.cinema.movie.controller;

import com.cinema.movie.dto.MovieResponseDto;
import com.cinema.movie.service.MovieService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{movieId}")
    public ResponseEntity<MovieResponseDto> getMovieById(@PathVariable String movieId) {
        // convert parameter to int
        int id = Integer.parseInt(movieId);
        return ResponseEntity.ok(movieService.getMovieById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<MovieResponseDto>> searchByTitle(@RequestParam String title) {
        return ResponseEntity.ok(movieService.searchByTitle(title));
    }

    @GetMapping("/filter/genre")
    public ResponseEntity<List<MovieResponseDto>> filterByGenre(@RequestParam String genre) {
        return ResponseEntity.ok(movieService.filterByGenre(genre));
    }

    @GetMapping("/filter/year")
    public ResponseEntity<List<MovieResponseDto>> filterByYear(@RequestParam Integer year) {
        return ResponseEntity.ok(movieService.filterByYear(year));
    }

}