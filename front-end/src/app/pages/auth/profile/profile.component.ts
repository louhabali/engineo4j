import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { switchMap, of } from 'rxjs';

import { UserService } from '../../../core/services/user.service';
import { MovieService } from '../../../core/services/movie.service';
import { UserProfile, UserProfileUpdateRequest } from '../../../models/user.model';
import { MovieCard } from '../../../models/movie.model';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './profile.component.html'
})
export class ProfileComponent implements OnInit {

  private readonly userService = inject(UserService);
  private readonly movieService = inject(MovieService);
  private readonly router = inject(Router);

  activeTab: 'watchlist' | 'favorites' | 'settings' = 'watchlist';

  user: UserProfile = {
    fullName: '',
    email: '',
    memberSince: '',
    totalWatchlist: 0,
    totalFavorites: 0,
    totalRatings: 0
  };

  settingsForm: UserProfileUpdateRequest = {
    fullName: '',
    email: ''
  };

  watchlist: MovieCard[] = [];

  successMessage = '';
  errorMessage = '';

  isLoading = false;
  isLoadingWatchlist = false;
  isSaving = false;

  ngOnInit(): void {
    this.loadProfile();
    this.loadWatchlist();
  }

  /**
   * Fetch Watchlist IDs from user-service and hydrate using movie-service batch API.
   */
  loadWatchlist(): void {
    this.isLoadingWatchlist = true;

    this.userService.getWatchlistMovieIds()
      .pipe(
        switchMap((ids: number[]) => {
          if (!ids || ids.length === 0) {
            return of([]); // Skip HTTP call if user has no saved items
          }
          return this.movieService.getMoviesByIds(ids);
        })
      )
      .subscribe({
        next: (movies: MovieCard[]) => {
          this.watchlist = movies;
          this.isLoadingWatchlist = false;
        },
        error: (error) => {
          console.error('Failed to load watchlist:', error);
          this.isLoadingWatchlist = false;
        }
      });
  }

  /**
   * Remove item from backend watchlist and update component state instantly.
   */
  removeFromWatchlist(movieId: number, event: Event): void {
    event.stopPropagation();
    event.preventDefault();

    this.userService.removeFromWatchlist(movieId).subscribe({
      next: () => {
        // Filter out removed movie locally without re-fetching everything
        this.watchlist = this.watchlist.filter(movie => movie.id !== movieId);
        if (this.user) {
          this.user.totalWatchlist = Math.max(0, this.user.totalWatchlist - 1);
        }
      },
      error: (error) => {
        console.error('Failed to remove movie from watchlist:', error);
      }
    });
  }

  private loadProfile(): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.userService.getUserProfile().subscribe({
      next: (profile: UserProfile) => {
        this.user = profile;
        this.settingsForm = {
          fullName: profile.fullName,
          email: profile.email
        };
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Failed to load profile:', error);
        this.isLoading = false;
        this.errorMessage = 'Could not load profile details.';
        if (error.status === 401) {
          this.onLogout();
        }
      }
    });
  }

  setTab(tab: 'watchlist' | 'favorites' | 'settings'): void {
    this.activeTab = tab;
    this.successMessage = '';
    this.errorMessage = '';
  }

  onSaveSettings(): void {
    if (this.isSaving) return;

    this.successMessage = '';
    this.errorMessage = '';

    const request: UserProfileUpdateRequest = {
      fullName: this.settingsForm.fullName.trim(),
      email: this.settingsForm.email.trim()
    };

    if (!request.fullName || !request.email) {
      this.errorMessage = 'Full name and email are required.';
      return;
    }

    this.isSaving = true;

    this.userService.updateUserProfile(request).subscribe({
      next: (updatedProfile: UserProfile) => {
        this.user = updatedProfile;
        this.settingsForm = {
          fullName: updatedProfile.fullName,
          email: updatedProfile.email
        };
        this.isSaving = false;
        this.successMessage = 'Profile updated successfully.';
        setTimeout(() => (this.successMessage = ''), 3000);
      },
      error: (error) => {
        this.isSaving = false;
        this.errorMessage = 'Failed to update profile settings.';
      }
    });
  }

  onLogout(): void {
    this.userService.logout();
    this.router.navigate(['/login']);
  }
}