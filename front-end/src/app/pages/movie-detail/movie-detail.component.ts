import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';

import {
  MovieDetail,
  RelatedGraphMovie
} from '../../models/movie.model';

import { MovieService } from '../../core/services/movie.service';
import { RatingService } from '../../core/services/rating.service';
import { UserService } from '../../core/services/user.service';

@Component({
  selector: 'app-movie-detail',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './movie-detail.component.html'
})
export class MovieDetailComponent implements OnInit {

  private route = inject(ActivatedRoute);
  private movieService = inject(MovieService);
  private ratingService = inject(RatingService);
  private userService = inject(UserService);

  movieId!: number;

  hoveredStar = 0;
  isWatchlisted = false;
  isWatchlistLoading = false;
  isRatingSubmitting = false;
  ratingMessage = '';
  ratingError = '';

  movie: MovieDetail = {
    id: 0,
    title: 'Loading...',
    tagline: '',
    synopsis: '',
    averageRating: 0,
    releaseYear: 2026,
    duration: '',
    director: '',
    genres: [],
    bannerUrl: '',
    posterUrl: '',
    userRating: 0
  };

  graphConnectedMovies: RelatedGraphMovie[] = [];

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');

    if (!idParam) {
      console.error('Movie ID not found in route');
      return;
    }

    this.movieId = Number(idParam);

    if (isNaN(this.movieId)) {
      console.error('Invalid movie ID:', idParam);
      return;
    }

    this.fetchMovieDetails(this.movieId);
    this.fetchGraphRecommendations(this.movieId);
    this.checkWatchlistStatus(this.movieId);
  }

  // --------------------------------------------------
  // Movie Details
  // --------------------------------------------------

  fetchMovieDetails(id: number): void {
    this.movieService.getMovieById(id).subscribe({
      next: (data) => {
        this.movie = data;
        this.hoveredStar = this.movie.userRating || 0;
      },
      error: (err) => {
        console.error('Failed to fetch movie details:', err);
      }
    });
  }

  // --------------------------------------------------
  // Graph Recommendations
  // --------------------------------------------------

  fetchGraphRecommendations(id: number): void {
    this.movieService.getGraphRecommendations(id).subscribe({
      next: (data) => {
        this.graphConnectedMovies = data;
      },
      error: (err) => {
        console.error('Failed to fetch graph recommendations:', err);
      }
    });
  }

  // --------------------------------------------------
  // Watchlist
  // --------------------------------------------------

  checkWatchlistStatus(movieId: number): void {

    if (!this.userService.isAuthenticated()) {
      this.isWatchlisted = false;
      return;
    }

    this.userService.isInWatchlist(movieId).subscribe({
      next: (response) => {

      
        this.isWatchlisted = response;
      },

      error: (err) => {
        console.error(
          'Failed to check watchlist status:',
          err
        );

        this.isWatchlisted = false;
      }
    });
  }

  toggleWatchlist(): void {

    if (!this.userService.isAuthenticated()) {
      console.warn('User must be authenticated to use the watchlist');
      return;
    }

    if (this.isWatchlistLoading) {
      return;
    }

    this.isWatchlistLoading = true;

    if (this.isWatchlisted) {
      this.removeFromWatchlist();
    } else {
      this.addToWatchlist();
    }
  }

  private addToWatchlist(): void {

    this.userService.addToWatchlist(this.movieId).subscribe({
      next: () => {
        this.isWatchlisted = true;
        this.isWatchlistLoading = false;

        console.log(
          `Movie ${this.movieId} added to watchlist`
        );
      },

      error: (err) => {
        this.isWatchlistLoading = false;

        console.error(
          'Failed to add movie to watchlist:',
          err
        );
      }
    });
  }

  private removeFromWatchlist(): void {

    this.userService.removeFromWatchlist(this.movieId).subscribe({
      next: () => {
        this.isWatchlisted = false;
        this.isWatchlistLoading = false;

        console.log(
          `Movie ${this.movieId} removed from watchlist`
        );
      },

      error: (err) => {
        this.isWatchlistLoading = false;

        console.error(
          'Failed to remove movie from watchlist:',
          err
        );
      }
    });
  }

  // --------------------------------------------------
  // Rating
  // --------------------------------------------------

  setRating(rating: number): void {
    this.ratingMessage = '';
    this.ratingError = '';

    if (!this.userService.isAuthenticated()) {
      this.ratingError = 'Please log in to rate this movie.';
      return;
    }

    if (this.isRatingSubmitting) return;

    this.isRatingSubmitting = true;
    this.ratingService.submitRating({ movieId: this.movieId, score: rating }).subscribe({
      next: (savedRating) => {
        this.movie.userRating = savedRating.score;
        this.hoveredStar = savedRating.score;
        this.ratingMessage = 'Your rating has been saved.';
        this.isRatingSubmitting = false;
      },
      error: (error) => {
        this.ratingError = error.error?.message || 'Could not save your rating. Please try again.';
        this.hoveredStar = this.movie.userRating || 0;
        this.isRatingSubmitting = false;
      }
    });
  }
}
