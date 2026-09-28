package com.cinema.movie.service.impl;

import com.cinema.movie.dto.MovieCardDto;
import com.cinema.movie.dto.MovieResponseDto;
import com.cinema.movie.entity.MovieEntity;
import com.cinema.movie.exception.MovieNotFoundException;
import com.cinema.movie.repository.MovieRepository;
import com.cinema.movie.service.MovieService;
import com.cinema.movie.service.MovieSpecifications;

import jakarta.transaction.Transactional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    // --------------------------------------------------
    // Get all movies
    // --------------------------------------------------

    @Override
    public List<MovieResponseDto> getAllMovies() {

        return movieRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    // --------------------------------------------------
    // Get paginated movies
    // --------------------------------------------------

    @Override
    @Cacheable(
        value = "moviesCache",
        key = "#page + '-' + #size"
    )
    public List<MovieResponseDto> getPaginatedMovies(
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        return movieRepository.findAll(pageable)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    // --------------------------------------------------
    // Get movie by ID
    // --------------------------------------------------

    @Override
    public MovieResponseDto getMovieById(long movieId) {

        return movieRepository.findById(movieId)
                .map(this::mapToDto)
                .orElseThrow(
                    () -> new MovieNotFoundException(
                        "Movie not found with ID: " + movieId
                    )
                );
    }

    // --------------------------------------------------
    // Search movies
    // --------------------------------------------------

    @Override
    public List<MovieResponseDto> searchMovies(
            String title,
            String genre,
            Integer releaseYear,
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        Specification<MovieEntity> specification =
                MovieSpecifications.filterMovies(
                    title,
                    genre,
                    releaseYear
                );

        return movieRepository.findAll(
                    specification,
                    pageable
                )
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    // --------------------------------------------------
    // Get movie cards by IDs
    // --------------------------------------------------

    @Override
    @Transactional
    public List<MovieCardDto> getMovieCardsByIds(
            List<Long> ids
    ) {

        if (ids == null || ids.isEmpty()) {
            return List.of();
        }

        List<MovieEntity> movies =
                movieRepository.findAllById(ids);

        return movies.stream()
                .map(this::mapToMovieCardDto)
                .toList();
    }

    // --------------------------------------------------
    // Movie Response DTO mapper
    // --------------------------------------------------

    private MovieResponseDto mapToDto(
            MovieEntity entity
    ) {

        return MovieResponseDto.builder()
                .id(entity.getId())
                .movieId(entity.getMovieId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .tagline(entity.getTagline())
                .duration(entity.getDuration())
                .director(entity.getDirector())
                .genres(
                    entity.getGenres() != null
                        ? new ArrayList<>(entity.getGenres())
                        : null
                )
                .releaseYear(entity.getReleaseYear())
                .averageRating(entity.getAverageRating())
                .bannerUrl(entity.getBannerUrl())
                .posterUrl(entity.getPosterUrl())
                .build();
    }

    // --------------------------------------------------
    // Movie Card DTO mapper
    // --------------------------------------------------

    private MovieCardDto mapToMovieCardDto(
            MovieEntity entity
    ) {

        return MovieCardDto.builder()
                .id(entity.getId())
                .movieId(entity.getMovieId())
                .title(entity.getTitle())
                .releaseYear(entity.getReleaseYear())
                .averageRating(entity.getAverageRating())
                .genres(
                    entity.getGenres() != null
                        ? new ArrayList<>(entity.getGenres())
                        : null
                )
                .posterUrl(entity.getPosterUrl())
                .bannerUrl(entity.getBannerUrl())
                .build();
    }
}