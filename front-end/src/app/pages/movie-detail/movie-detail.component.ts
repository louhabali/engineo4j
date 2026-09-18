import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { MovieDetail, RelatedGraphMovie } from '../../models/movie.model';
import { MovieService } from '../../core/services/movie.service'; 

@Component({
  selector: 'app-movie-detail',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './movie-detail.component.html'
})
export class MovieDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private movieService = inject(MovieService);

  movieId!: string | number;
  hoveredStar = 0;
  isWatchlisted = false;

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
    // Grab the ID from the route
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.movieId = idParam;
      this.fetchMovieDetails(this.movieId);
      this.fetchGraphRecommendations(this.movieId);
    }
  }

  fetchMovieDetails(id: string | number): void {

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

  fetchGraphRecommendations(id: string | number): void {

    this.movieService.getGraphRecommendations(id).subscribe({
      next: (data) => {
        this.graphConnectedMovies = data;
      },
      error: (err) => {
        console.error('Failed to fetch graph recommendations:', err);
      }
    });
  }

  toggleWatchlist(): void {
    this.isWatchlisted = !this.isWatchlisted;
    
  }

  setRating(rating: number): void {
    this.movie.userRating = rating;
    console.log(`Updated rating for movie ${this.movieId} to ${rating} stars`);
    // TODO: Send user rating update to backend API
  }
}