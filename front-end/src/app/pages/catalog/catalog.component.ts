import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { FeaturedMovie, MovieCard } from '../../models/movie.model';
import { MovieService } from '../../core/services/movie.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './catalog.component.html'
})
export class CatalogComponent implements OnInit {
  private movieService = inject(MovieService);

  searchQuery = '';
  selectedGenre = 'ALL';

  heroMovie: FeaturedMovie | null = null;
  genres: string[] = ['ALL', 'ACTION', 'SCI-FI', 'ANIMATION', 'THRILLER', 'DRAMA'];
  trendingMovies: MovieCard[] = [];
  graphRecommendations: MovieCard[] = [];

  ngOnInit(): void {
    this.loadMovies();
  }

  loadMovies(): void {
    this.movieService.getAllMovies().subscribe({
      next: (movies) => {
        this.trendingMovies = movies;
        // Optionally set the first movie as the hero if available
        if (movies.length > 0) {
          const first = movies[0];
          this.heroMovie = {
            id: Number(first.id) || 101,
            title: first.title,
            tagline: 'FEATURED MOVIE FROM YOUR CATALOG',
            description: 'Explore the latest additions to your cinematic collection.',
            rating: first.rating || 8.0,
            year: first.releaseYear || 2024,
            matchPercentage: 98,
            bannerUrl: first.bannerUrl || 'spiderbg.webp'
          };
        }
      },
      error: (err) => console.error('Failed to load movies from backend:', err)
    });
  }

  onGenreSelect(genre: string): void {
    this.selectedGenre = genre;
    if (genre === 'ALL') {
      this.loadMovies();
    } else {
      this.movieService.filterByGenre(genre).subscribe({
        next: (movies) => this.trendingMovies = movies,
        error: (err) => console.error('Genre filter failed:', err)
      });
    }
  }

  onSearch(): void {
    if (!this.searchQuery.trim()) {
      this.loadMovies();
      return;
    }

    this.movieService.searchByTitle(this.searchQuery).subscribe({
      next: (results) => this.trendingMovies = results,
      error: (err) => console.error('Search query failed:', err)
    });
  }
}