import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { GraphRecommendation } from '../../models/recommendations.model';
import { RatingService } from '../../core/services/rating.service';
import { UserService } from '../../core/services/user.service';
import { RecommendationsService } from '../../core/services/recommendations.service';

@Component({
  selector: 'app-recommendations',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './recommendations.component.html'
})
export class RecommendationsComponent implements OnInit {
  private readonly ratingService = inject(RatingService);
  private readonly userService = inject(UserService);
  private readonly recommendationsService = inject(RecommendationsService);

  recommendations: GraphRecommendation[] = [];
  isLoading = false;
  loadError = '';

  selectedRatingItem: GraphRecommendation | null = null;
  hoveredStar = 0;
  isRatingSubmitting = false;
  ratingError = '';

  ngOnInit(): void {
    this.loadRecommendations();
  }

  loadRecommendations(): void {
    this.isLoading = true;
    this.loadError = '';

    this.recommendationsService.getRecommendations().subscribe({
      next: (recommendations) => {
        this.recommendations = recommendations;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Failed to load recommendations:', error);
        this.loadError = 'Could not load recommendations. Please try again later.';
        this.isLoading = false;
      },
    });
  }

  openRatingModal(item: GraphRecommendation): void {
    this.selectedRatingItem = item;
    this.hoveredStar = item.userRating || 0;
    this.ratingError = '';
  }

  closeRatingModal(): void {
    if (this.isRatingSubmitting) return;
    this.selectedRatingItem = null;
    this.hoveredStar = 0;
    this.ratingError = '';
  }

  setRating(rating: number): void {
    const item = this.selectedRatingItem;
    if (!item || this.isRatingSubmitting) return;

    this.ratingError = '';
    if (!this.userService.isAuthenticated()) {
      this.ratingError = 'Please log in to rate this movie.';
      return;
    }

    this.isRatingSubmitting = true;
    this.ratingService.submitRating({ movieId: item.id, score: rating }).subscribe({
      next: (savedRating) => {
        item.userRating = savedRating.score;
        this.isRatingSubmitting = false;
        this.closeRatingModal();
      },
      error: (error) => {
        this.ratingError = error.error?.message || 'Could not save your rating. Please try again.';
        this.isRatingSubmitting = false;
      }
    });
  }
}
