import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

export interface Movie {
  id: number;
  title: string;
  year: number;
  rating: number;
  posterUrl: string;
  genre: string;
}

export interface UserProfile {
  fullName: string;
  email: string;
  memberSince: string;
  totalWatchlist: number;
  totalFavorites: number;
  totalRatings: number;
}

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './profile.component.html'
})
export class ProfileComponent implements OnInit {
  activeTab: 'watchlist' | 'favorites' | 'settings' = 'watchlist';

  user: UserProfile = {
    fullName: 'John Doe',
    email: 'user@engineo.io',
    memberSince: '2026',
    totalWatchlist: 18,
    totalFavorites: 12,
    totalRatings: 42
  };

  watchlist: Movie[] = [
    { id: 1, title: 'Spider-Man: Across the Spider-Verse', year: 2023, rating: 8.7, genre: 'Animation / Sci-Fi', posterUrl: 'https://image.tmdb.org/t/p/w500/8Vt6mL92LXYR23ChHYWF2O2fdbX.jpg' },
    { id: 2, title: 'The Dark Knight', year: 2008, rating: 9.0, genre: 'Action / Crime', posterUrl: 'https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg' },
    { id: 3, title: 'Interstellar', year: 2014, rating: 8.6, genre: 'Sci-Fi / Drama', posterUrl: 'https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg' },
    { id: 4, title: 'Inception', year: 2010, rating: 8.8, genre: 'Sci-Fi / Action', posterUrl: 'https://image.tmdb.org/t/p/w500/oYuLE1311o2R3B2S9M39fUxS231.jpg' }
  ];

  favorites: Movie[] = [
    { id: 1, title: 'Spider-Man: Into the Spider-Verse', year: 2018, rating: 8.4, genre: 'Animation / Action', posterUrl: 'https://image.tmdb.org/t/p/w500/iiZZdoQH211fiOpP39Tz3S2L1q5.jpg' },
    { id: 2, title: 'Blade Runner 2049', year: 2017, rating: 8.0, genre: 'Sci-Fi / Mystery', posterUrl: 'https://image.tmdb.org/t/p/w500/gA9L1AS22P9S215L1A1S1A1S1A.jpg' }
  ];

  successMessage = '';

  constructor(private router: Router) {}

  ngOnInit(): void {
    // TODO: Load profile data & user cinema lists from backend endpoints
  }

  setTab(tab: 'watchlist' | 'favorites' | 'settings'): void {
    this.activeTab = tab;
  }

  onSaveSettings(): void {
    this.successMessage = 'Profile node preferences saved successfully.';
    setTimeout(() => (this.successMessage = ''), 3000);
  }

  onLogout(): void {
    // TODO: Clear local storage tokens
    this.router.navigate(['/login']);
  }
}