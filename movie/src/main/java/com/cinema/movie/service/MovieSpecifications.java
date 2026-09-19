package com.cinema.movie.service;

import com.cinema.movie.entity.MovieEntity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Expression;

public class MovieSpecifications {

    public static Specification<MovieEntity> filterMovies(String query, String genre, Integer releaseYear) {
        return (root, queryCriteria, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Global match for title, year, or elements inside the genres
            if (query != null && !query.trim().isEmpty()) {
                String pattern = "%" + query.trim().toLowerCase() + "%";
                
                Predicate titleMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), pattern);
                
                // Join the element collection for genre-text matching
                Expression<String> genresCollection = root.join("genres");
                Predicate genreMatch = criteriaBuilder.like(criteriaBuilder.lower(genresCollection), pattern);
                
                // Check if the query can be parsed as a release year
                Predicate yearMatch = null;
                try {
                    Integer yearVal = Integer.parseInt(query.trim());
                    yearMatch = criteriaBuilder.equal(root.get("releaseYear"), yearVal);
                } catch (NumberFormatException ignored) {}

                Predicate globalSearch = yearMatch != null 
                    ? criteriaBuilder.or(titleMatch, genreMatch, yearMatch)
                    : criteriaBuilder.or(titleMatch, genreMatch);
                    
                predicates.add(globalSearch);
            }

            //  pill filter buttons
            if (genre != null && !genre.equalsIgnoreCase("ALL")) {
                Expression<String> genresCollection = root.join("genres");
                predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(genresCollection), genre.toLowerCase()));
            }

            //  year filter if explicitly specified
            if (releaseYear != null) {
                predicates.add(criteriaBuilder.equal(root.get("releaseYear"), releaseYear));
            }

            // Prevent duplicate movie records in paginated results due to collection joins
            queryCriteria.distinct(true);

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}