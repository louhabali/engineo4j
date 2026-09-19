package com.cinema.movie.service;

import com.cinema.movie.dto.MovieResponseDto;

import java.util.List;

public interface MovieService {
    List<MovieResponseDto> getAllMovies();
    MovieResponseDto getMovieById(long id);
    List<MovieResponseDto> getPaginatedMovies(int page, int size);
    List<MovieResponseDto> searchMovies(String title, String genre, Integer releaseYear, int page, int size);
}