package com.cinema.movie.repository;

import com.cinema.movie.entity.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<MovieEntity, Long> {

    Optional<MovieEntity> findById(Long id);

    List<MovieEntity> findByTitleContainingIgnoreCase(String title);

    List<MovieEntity> findByReleaseYear(Integer releaseYear);

    List<MovieEntity> findByGenresContaining(String genre);
}