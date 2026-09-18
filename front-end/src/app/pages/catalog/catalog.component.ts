import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { FeaturedMovie, MovieCard } from '../../models/movie.model';



@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './catalog.component.html'
})
export class CatalogComponent implements OnInit {
  searchQuery = '';
  selectedGenre = 'ALL';

  heroMovie: FeaturedMovie = {
    id: 101,
    title: 'SPIDER-MAN: ACROSS THE SPIDER-VERSE',
    tagline: 'MILES MORALES RETURNS FOR AN EPIC MULTIVERSE TRAVERSAL',
    description: 'Miles Morales catapults across the Multiverse, where he encounters a team of Spider-People charged with protecting its very existence.',
    rating: 8.7,
    year: 2023,
    matchPercentage: 99,
    bannerUrl: 'spiderbg.webp'
  };

  genres: string[] = ['ALL', 'ACTION', 'SCI-FI', 'ANIMATION', 'THRILLER', 'DRAMA'];

  trendingMovies: MovieCard[] = [
    { id: 1, title: 'Across the Spider-Verse', year: 2023, rating: 8.7, genre: 'Animation / Sci-Fi', posterUrl: 'https://image.tmdb.org/t/p/w500/8Vt6mL92LXYR23ChHYWF2O2fdbX.jpg' },
    { id: 2, title: 'The Dark Knight', year: 2008, rating: 9.0, genre: 'Action / Crime', posterUrl: 'https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg' },
    { id: 3, title: 'Interstellar', year: 2014, rating: 8.6, genre: 'Sci-Fi / Drama', posterUrl: 'https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg' },
    { id: 4, title: 'Inception', year: 2010, rating: 8.8, genre: 'Sci-Fi / Action', posterUrl: 'https://image.tmdb.org/t/p/w500/oYuLE1311o2R3B2S9M39fUxS231.jpg' }
  ];

  graphRecommendations: MovieCard[] = [
    { id: 5, title: 'Blade Runner 2049', year: 2017, rating: 8.0, genre: 'Sci-Fi / Mystery', posterUrl: 'https://image.tmdb.org/t/p/w500/gA9L1AS22P9S215L1A1S1A1S1A.jpg' },
    { id: 6, title: 'The Matrix', year: 1999, rating: 8.7, genre: 'Sci-Fi / Action', posterUrl: 'https://image.tmdb.org/t/p/w500/f89U339R3S129S39S.jpg' },
    { id: 7, title: 'Dune: Part Two', year: 2024, rating: 8.5, genre: 'Sci-Fi / Adventure', posterUrl: 'https://image.tmdb.org/t/p/w500/1pdfLvk3R9S.jpg' },
    { id: 8, title: 'Cyberpunk: Edgerunners', year: 2022, rating: 8.3, genre: 'Animation / Action', posterUrl: 'https://image.tmdb.org/t/p/w500/7S34S.jpg' }
  ];

  ngOnInit(): void {
    // TODO: Fetch graph recommendations from Neo4j/Spring Boot endpoint
  }

  onGenreSelect(genre: string): void {
    this.selectedGenre = genre;
  }

  onSearch(): void {
    console.log('Querying graph for:', this.searchQuery);
  }
}