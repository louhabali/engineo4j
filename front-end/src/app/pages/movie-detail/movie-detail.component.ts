import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';

export interface MovieDetail {
  id: number;
  title: string;
  tagline: string;
  synopsis: string;
  rating: number;
  releaseYear: number;
  duration: string;
  director: string;
  genres: string[];
  bannerUrl: string;
  posterUrl: string;
  userRating?: number;
}

export interface RelatedGraphMovie {
  id: number;
  title: string;
  posterUrl: string;
  affinityScore: number;
  connectionReason: string;
}

@Component({
  selector: 'app-movie-detail',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './movie-detail.component.html'
})
export class MovieDetailComponent implements OnInit {
  movieId!: number;
  hoveredStar = 0;
  isWatchlisted = false;

  movie: MovieDetail = {
    id: 101,
    title: 'SPIDER-MAN: ACROSS THE SPIDER-VERSE',
    tagline: 'IT\'S HOW YOU WEAR THE MASK THAT MATTERS',
    synopsis: 'Miles Morales catapults across the Multiverse, where he encounters a team of Spider-People charged with protecting its very existence. When the heroes clash on how to handle a new threat, Miles must redefine what it means to be a hero.',
    rating: 8.7,
    releaseYear: 2023,
    duration: '2h 20m',
    director: 'Joaquim Dos Santos, Kemp Powers, Justin K. Thompson',
    genres: ['Animation', 'Action', 'Adventure', 'Sci-Fi'],
    bannerUrl: 'spiderbg.webp',
    posterUrl: 'https://image.tmdb.org/t/p/w500/8Vt6mL92LXYR23ChHYWF2O2fdbX.jpg',
    userRating: 5
  };

  graphConnectedMovies: RelatedGraphMovie[] = [
    { id: 102, title: 'Into the Spider-Verse', posterUrl: 'https://image.tmdb.org/t/p/w500/iiZZdoQH211fiOpP39Tz3S2L1q5.jpg', affinityScore: 99, connectionReason: 'Direct Franchise Node' },
    { id: 103, title: 'Cyberpunk: Edgerunners', posterUrl: 'https://image.tmdb.org/t/p/w500/7S34S.jpg', affinityScore: 91, connectionReason: 'Shared Visual Style & Sci-Fi Genre' },
    { id: 104, title: 'The Matrix', posterUrl: 'https://image.tmdb.org/t/p/w500/f89U339R3S129S39S.jpg', affinityScore: 88, connectionReason: 'High Collaborative User Overlap' },
    { id: 105, title: 'Dune: Part Two', posterUrl: 'https://image.tmdb.org/t/p/w500/1pdfLvk3R9S.jpg', affinityScore: 84, connectionReason: 'Recommended by Matrix Factorization' }
  ];

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.movieId = Number(this.route.snapshot.paramMap.get('id'));
    this.hoveredStar = this.movie.userRating || 0;
    // TODO: Fetch movie details & Neo4j graph relationships by ID from backend
  }

  toggleWatchlist(): void {
    this.isWatchlisted = !this.isWatchlisted;
  }

  setRating(rating: number): void {
    this.movie.userRating = rating;
    console.log(`Updated rating for movie ${this.movieId} to ${rating} stars`);
  }
}