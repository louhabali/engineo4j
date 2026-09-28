import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

import { UserService } from '../../../core/services/user.service';
import {
  UserProfile,
  UserProfileUpdateRequest
} from '../../../models/user.model';

export interface Movie {
  id: number;
  title: string;
  year: number;
  rating: number;
  posterUrl: string;
  genre: string;
}

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterLink
  ],
  templateUrl: './profile.component.html'
})
export class ProfileComponent implements OnInit {

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

  watchlist: Movie[] = [
    {
      id: 1,
      title: 'Spider-Man: Across the Spider-Verse',
      year: 2023,
      rating: 8.7,
      genre: 'Animation / Sci-Fi',
      posterUrl:
        'https://image.tmdb.org/t/p/w500/8Vt6mL92LXYR23ChHYWF2O2fdbX.jpg'
    },
    {
      id: 2,
      title: 'The Dark Knight',
      year: 2008,
      rating: 9.0,
      genre: 'Action / Crime',
      posterUrl:
        'https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg'
    },
    {
      id: 3,
      title: 'Interstellar',
      year: 2014,
      rating: 8.6,
      genre: 'Sci-Fi / Drama',
      posterUrl:
        'https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg'
    },
    {
      id: 4,
      title: 'Inception',
      year: 2010,
      rating: 8.8,
      genre: 'Sci-Fi / Action',
      posterUrl:
        'https://image.tmdb.org/t/p/w500/oYuLE1311o2R3B2S9M39fUxS231.jpg'
    }
  ];

  favorites: Movie[] = [
    {
      id: 1,
      title: 'Spider-Man: Into the Spider-Verse',
      year: 2018,
      rating: 8.4,
      genre: 'Animation / Action',
      posterUrl:
        'https://image.tmdb.org/t/p/w500/iiZZdoQH211fiOpP39Tz3S2L1q5.jpg'
    },
    {
      id: 2,
      title: 'Blade Runner 2049',
      year: 2017,
      rating: 8.0,
      genre: 'Sci-Fi / Mystery',
      posterUrl:
        'https://image.tmdb.org/t/p/w500/gA9L1AS22P9S215L1A1S1A1S1A.jpg'
    }
  ];

  successMessage = '';
  errorMessage = '';

  isLoading = false;
  isSaving = false;

  constructor(
    private readonly userService: UserService,
    private readonly router: Router
  ) {}

  ngOnInit(): void {
    this.loadProfile();
  }

  /**
   * Load the authenticated user's profile.
   */
  private loadProfile(): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.userService.getUserProfile().subscribe({
      next: (profile: UserProfile) => {
        this.user = profile;

        // Initialize the settings form from backend data.
        this.settingsForm = {
          fullName: profile.fullName,
          email: profile.email
        };

        this.isLoading = false;
      },

      error: (error) => {
        console.error('Failed to load profile:', error);

        this.isLoading = false;
        this.errorMessage =
          'Could not load profile details. Please try again.';

        // Token is invalid/expired.
        if (error.status === 401) {
          this.onLogout();
        }
      }
    });
  }

  /**
   * Change the active profile tab.
   */
  setTab(
    tab: 'watchlist' | 'favorites' | 'settings'
  ): void {
    this.activeTab = tab;

    this.successMessage = '';
    this.errorMessage = '';
  }

  /**
   * Save profile settings.
   */
  onSaveSettings(): void {
    if (this.isSaving) {
      return;
    }

    this.successMessage = '';
    this.errorMessage = '';

    const request: UserProfileUpdateRequest = {
      fullName: this.settingsForm.fullName.trim(),
      email: this.settingsForm.email.trim()
    };

    if (!request.fullName) {
      this.errorMessage = 'Full name is required.';
      return;
    }

    if (!request.email) {
      this.errorMessage = 'Email address is required.';
      return;
    }

    this.isSaving = true;

    this.userService.updateUserProfile(request).subscribe({
      next: (updatedProfile: UserProfile) => {
        // Replace the profile only after backend successfully updates it.
        this.user = updatedProfile;

        // Keep the form synchronized with the backend response.
        this.settingsForm = {
          fullName: updatedProfile.fullName,
          email: updatedProfile.email
        };

        this.isSaving = false;
        this.successMessage =
          'Profile node preferences saved successfully.';

        setTimeout(() => {
          this.successMessage = '';
        }, 3000);
      },

      error: (error) => {
        console.error('Failed to update profile:', error);

        this.isSaving = false;

        if (error.status === 400) {
          this.errorMessage =
            'Invalid profile information.';
        } else if (error.status === 401) {
          this.onLogout();
        } else if (error.status === 409) {
          this.errorMessage =
            'This email address is already in use.';
        } else {
          this.errorMessage =
            'Failed to update profile settings.';
        }
      }
    });
  }

  /**
   * Logout the current user.
   */
  onLogout(): void {
    this.userService.logout();
    this.router.navigate(['/login']);
  }
}
