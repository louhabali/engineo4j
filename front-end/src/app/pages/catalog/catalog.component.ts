import { Component, OnInit, inject , HostListener} from '@angular/core';
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

  currentPage = 0;
  pageSize = 8;
  isLoading = false;
  hasMore = true;

  ngOnInit(): void {
    this.loadMoreMovies();
  }

  loadMoreMovies(): void {
    if (this.isLoading || !this.hasMore) return;
    this.isLoading = true;

    this.movieService.getPaginatedMovies(this.currentPage, this.pageSize).subscribe({
      next: (movies) => {
        if (movies.length < this.pageSize) {
          this.hasMore = false;
        }
        this.trendingMovies = [...this.trendingMovies, ...movies];
        if (this.currentPage === 0 && movies.length > 0) {
          this.initHero(movies[0]);
        }
        this.currentPage++;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Failed to load paginated movies:', err);
        this.isLoading = false;
      }
    });
  }

  @HostListener('window:scroll', [])
  onScroll(): void {
    const scrollPosition = window.innerHeight + window.scrollY;
    const threshold = document.documentElement.scrollHeight - 200;
    
    if (scrollPosition >= threshold && !this.isLoading && this.selectedGenre === 'ALL' && !this.searchQuery) {
      this.loadMoreMovies();
    }
  }

  private initHero(first: MovieCard): void {
    this.heroMovie = {
      id: Number(first.id) || 101,
      title: first.title,
      tagline: 'FEATURED MOVIE FROM YOUR CATALOG',
      description: 'Explore the latest additions to your cinematic collection.',
      rating: first.averageRating || 8.0,
      year: first.releaseYear || 2024,
      matchPercentage: 98,
      bannerUrl: first.bannerUrl || 'spiderbg.webp'
    };
  }
  onSearch(): void {
    if (this.searchQuery.trim()) {
      this.movieService.searchByTitle(this.searchQuery).subscribe({
        next: (movies) => {
          this.trendingMovies = movies;
        },
        error: (err) => {
          console.error('Failed to search movies:', err);
        }
      });
    }
  }
  onGenreSelect(genre: string): void {
    this.selectedGenre = genre;
    if (this.selectedGenre === 'ALL') {
      this.currentPage = 0;
      this.trendingMovies = [];
      this.hasMore = true;
      this.loadMoreMovies();
    } else {
      this.movieService.filterByGenre(this.selectedGenre).subscribe({
        next: (movies) => {
          this.trendingMovies = movies;
          this.hasMore = false; // Disable infinite scroll for filtered results
        },
        error: (err) => {
          console.error('Failed to filter movies by genre:', err);
        }
      }); 
    }
  }
}