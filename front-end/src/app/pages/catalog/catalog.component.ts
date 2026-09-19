import { Component, OnInit, inject, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
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
  selectedYear?: number;
  heroMovie: FeaturedMovie | null = null;
  genres: string[] = ['ALL', 'ACTION', 'SCI-FI', 'ANIMATION', 'THRILLER', 'DRAMA'];
  trendingMovies: MovieCard[] = [];
  graphRecommendations: MovieCard[] = [];

  currentPage = 0;
  pageSize = 8;
  isLoading = false;
  isInitialLoading = true; // Controls 1-second full-screen loader
  hasMore = true;

  private searchSubject = new Subject<string>();

  ngOnInit(): void {
    this.searchSubject.pipe(
      debounceTime(600),
      distinctUntilChanged()
    ).subscribe(() => {
      this.loadMovies(true);
    });

    // Initial load with a polished cinematic splash/spinner delay of 1 second
    setTimeout(() => {
      this.loadMovies(true);
    }, 2000);
  }

  onSearchChange(): void {
    this.searchSubject.next(this.searchQuery);
  }

  loadMovies(isReset: boolean = false): void {
    if (this.isLoading || (!this.hasMore && !isReset)) return;

    if (isReset) {
      this.currentPage = 0;
      this.trendingMovies = [];
      this.hasMore = true;
    }

    this.isLoading = true;

    this.movieService.getFilteredMovies(
      this.searchQuery,
      this.selectedGenre,
      this.selectedYear,
      this.currentPage,
      this.pageSize
    ).subscribe({
      next: (movies) => {
        if (movies.length < this.pageSize) {
          this.hasMore = false;
        }
        
        this.trendingMovies = [...this.trendingMovies, ...movies];

        if (this.currentPage === 0 && movies.length > 0 && !this.heroMovie) {
          this.initHero(movies[0]);
        }

        this.currentPage++;
        this.isLoading = false;
        this.isInitialLoading = false; // Hide splash loader once data is ready
      },
      error: (err) => {
        console.error('Failed to load filtered movies:', err);
        this.isLoading = false;
        this.isInitialLoading = false;
      }
    });
  }

  @HostListener('window:scroll', [])
  onScroll(): void {
    const scrollPosition = window.innerHeight + window.scrollY;
    const threshold = document.documentElement.scrollHeight - 200;

    if (scrollPosition >= threshold && !this.isLoading && this.hasMore) {
      this.loadMovies(false);
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

  onGenreSelect(genre: string): void {
    this.selectedGenre = genre;
    this.loadMovies(true);
  }
}