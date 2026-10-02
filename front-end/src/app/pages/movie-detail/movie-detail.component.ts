import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';

import {
  MovieDetail,
  RelatedGraphMovie
} from '../../models/movie.model';

import { MovieService } from '../../core/services/movie.service';
import { RatingService } from '../../core/services/rating.service';
import { UserService } from '../../core/services/user.service';
import { AuthStateService } from '../../core/services/auth-state.service';
import { RatingSummary } from '../../models/rating.model';

@Component({
  selector: 'app-movie-detail',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './movie-detail.component.html'
})
export class MovieDetailComponent implements OnInit {

  private route = inject(ActivatedRoute);
  private movieService = inject(MovieService);
  private ratingService = inject(RatingService);
  private userService = inject(UserService);
  readonly authenticated$ = this.userService.authenticated$;
  private authState = inject(AuthStateService);
  private router = inject(Router);
  movieId!: number;

  hoveredStar = 0;
  readonly userRating = signal(0);
  isWatchlisted = false;
  isWatchlistLoading = false;
  isRatingSubmitting = false;
  ratingMessage = '';
  ratingError = '';
  isShareModalOpen = false;
  isShareSubmitting = false;
  friendEmail = '';
  shareMessage = '';
  shareError = '';
  ratingSummary: RatingSummary = {
    averageRating: 0, ratingCount: 0,
    movieId: 0, userRating: 0 // Initialize userId as an empty string
  };

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
    this.fetchRatingSummary(this.movieId);
    this.authState.validateSession().subscribe({
      next: (session) => {
        if (session.status === 'authenticated') {
          this.checkWatchlistStatus(this.movieId);
        } else {
          this.isWatchlisted = false;
        }
      },
      error: () => {
        this.isWatchlisted = false;
      },
    });
  }

  // --------------------------------------------------
  // Movie Details
  // --------------------------------------------------

  fetchMovieDetails(id: number): void {
    this.movieService.getMovieById(id).subscribe({
      next: (data) => {
        this.movie = data;
      },
      error: (err) => {
        console.error('Failed to fetch movie details:', err);
        if (err.status === 404) {
          this.router.navigate(['/404']);
        }
      }
    });
  }

  fetchRatingSummary(id: number): void {
    this.ratingService.getMovieRatingSummary(id).subscribe({
      next: (summary) => {
        this.ratingSummary = summary;
        this.userRating.set(summary.userRating ?? 0);
        this.hoveredStar = this.userRating();
      },
      error: (error) => {
        console.error('Failed to fetch movie rating summary:', error);
        this.ratingSummary = { averageRating: 0, ratingCount: 0, movieId: id, userRating: 0 };
        this.userRating.set(0);
        this.hoveredStar = 0;
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

  openShareModal(): void {
    this.friendEmail = '';
    this.shareError = '';
    this.shareMessage = '';
    this.isShareModalOpen = true;
  }

  closeShareModal(): void {
    if (this.isShareSubmitting) return;
    this.isShareModalOpen = false;
    this.shareError = '';
  }

  submitMovieShare(): void {
    if (this.isShareSubmitting || !this.userService.isAuthenticated()) {
      this.shareError = 'Please sign in to share a movie.';
      return;
    }

    this.isShareSubmitting = true;
    this.shareError = '';
    this.userService.shareMovie({
      movieId: Number(this.movie.id),
      friendEmail: this.friendEmail.trim(),
    }).subscribe({
      next: (response) => {
        this.shareMessage = response.message || 'Movie shared successfully';
        this.isShareSubmitting = false;
        this.isShareModalOpen = false;
        this.friendEmail = '';
      },
      error: (error) => {
        this.shareError = error.error?.message || 'Could not share this movie. Please try again.';
        this.isShareSubmitting = false;
      },
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
        this.userRating.set(savedRating.score);
        this.hoveredStar = savedRating.score;
        this.ratingMessage = 'Your rating has been saved.';
        this.fetchRatingSummary(this.movieId);
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
