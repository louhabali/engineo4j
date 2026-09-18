package com.cinema.movie.service.impl;

import com.cinema.movie.dto.MovieRequestDto;
import com.cinema.movie.dto.MovieResponseDto;
import com.cinema.movie.entity.MovieEntity;
import com.cinema.movie.exception.MovieNotFoundException;
import com.cinema.movie.repository.MovieRepository;
import com.cinema.movie.service.MovieService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public List<MovieResponseDto> getAllMovies() {
        return movieRepository.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }
    
    @Override
    public MovieResponseDto getMovieById(long movieId) {
        return movieRepository.findById(movieId)
                .map(this::mapToDto)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found with ID: " + movieId));
    }

    @Override
    public List<MovieResponseDto> searchByTitle(String title) {
        return movieRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public List<MovieResponseDto> filterByGenre(String genre) {
        return movieRepository.findByGenresContaining(genre).stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public List<MovieResponseDto> filterByYear(Integer year) {
        return movieRepository.findByReleaseYear(year).stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public MovieResponseDto createMovie(MovieRequestDto requestDto) {
        MovieEntity entity = MovieEntity.builder()
                .movieId(requestDto.getMovieId())
                .title(requestDto.getTitle())
                .genres(requestDto.getGenres())
                .releaseYear(requestDto.getReleaseYear())
                .averageRating(0.0)
                .build();

        return mapToDto(movieRepository.save(entity));
    }

    private MovieResponseDto mapToDto(MovieEntity entity) {
        return MovieResponseDto.builder()
                .id(entity.getId())
                .movieId(entity.getMovieId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .tagline(entity.getTagline())
                .duration(entity.getDuration())
                .director(entity.getDirector())
                .genres(entity.getGenres())
                .releaseYear(entity.getReleaseYear())
                .averageRating(entity.getAverageRating())
                .bannerUrl(entity.getBannerUrl())
                .posterUrl(entity.getPosterUrl())
                .build();
    }
}