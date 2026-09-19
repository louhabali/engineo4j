package com.cinema.movie.service.impl;

import com.cinema.movie.dto.MovieRequestDto;
import com.cinema.movie.dto.MovieResponseDto;
import com.cinema.movie.entity.MovieEntity;
import com.cinema.movie.exception.MovieNotFoundException;
import com.cinema.movie.repository.MovieRepository;
import com.cinema.movie.service.MovieService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import com.cinema.movie.service.MovieSpecifications;
import java.util.ArrayList;
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
    @Cacheable(value = "moviesCache", key = "#page + '-' + #size")
    public List<MovieResponseDto> getPaginatedMovies(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return movieRepository.findAll(pageable).stream()
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
    public List<MovieResponseDto> searchMovies(String title, String genre, Integer releaseYear, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Specification<MovieEntity> spec = MovieSpecifications.filterMovies(title, genre, releaseYear);

    return movieRepository.findAll(spec, pageable)
            .stream()
            .map(this::mapToDto) 
            .toList();
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
                .genres(entity.getGenres() != null ? new ArrayList<>(entity.getGenres()) : null)
                .releaseYear(entity.getReleaseYear())
                .averageRating(entity.getAverageRating())
                .bannerUrl(entity.getBannerUrl())
                .posterUrl(entity.getPosterUrl())
                .build();
    }
}