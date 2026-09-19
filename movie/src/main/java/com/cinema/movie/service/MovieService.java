package com.cinema.movie.service;

import com.cinema.movie.dto.MovieRequestDto;
import com.cinema.movie.dto.MovieResponseDto;

import java.util.List;

public interface MovieService {
    List<MovieResponseDto> getAllMovies();
    MovieResponseDto getMovieById(long id);
    List<MovieResponseDto> searchByTitle(String title);
    List<MovieResponseDto> filterByGenre(String genre);
    List<MovieResponseDto> filterByYear(Integer year);
    MovieResponseDto createMovie(MovieRequestDto requestDto);
    List<MovieResponseDto> getPaginatedMovies(int page, int size);
}