import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

export interface GraphRecommendation {
  id: number;
  title: string;
  year: number;
  genre: string;
  posterUrl: string;
  affinityScore: number;
  connectedNode: string;
  userRating?: number;
}

@Component({
  selector: 'app-recommendations',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './recommendations.component.html'
})
export class RecommendationsComponent implements OnInit {
  recommendations: GraphRecommendation[] = [
    { id: 101, title: 'Spider-Man: Into the Spider-Verse', year: 2018, genre: 'Animation / Action', posterUrl: 'https://image.tmdb.org/t/p/w500/iiZZdoQH211fiOpP39Tz3S2L1q5.jpg', affinityScore: 98, connectedNode: 'Spider-Man: Across the Spider-Verse' },
    { id: 102, title: 'Blade Runner 2049', year: 2017, genre: 'Sci-Fi / Mystery', posterUrl: 'https://image.tmdb.org/t/p/w500/gA9L1AS22P9S215L1A1S1A1S1A.jpg', affinityScore: 94, connectedNode: 'Interstellar' },
    { id: 103, title: 'The Matrix', year: 1999, genre: 'Sci-Fi / Action', posterUrl: 'https://image.tmdb.org/t/p/w500/f89U339R3S129S39S.jpg', affinityScore: 91, connectedNode: 'Inception' },
    { id: 104, title: 'Dune: Part Two', year: 2024, genre: 'Sci-Fi / Adventure', posterUrl: 'https://image.tmdb.org/t/p/w500/1pdfLvk3R9S.jpg', affinityScore: 89, connectedNode: 'Interstellar' }
  ];

  selectedRatingItem: GraphRecommendation | null = null;
  hoveredStar = 0;

  ngOnInit(): void {
    // TODO: Fetch recommendations from backend
  }

  openRatingModal(item: GraphRecommendation): void {
    this.selectedRatingItem = item;
    this.hoveredStar = item.userRating || 0;
  }

  closeRatingModal(): void {
    this.selectedRatingItem = null;
    this.hoveredStar = 0;
  }

  setRating(rating: number): void {
    if (this.selectedRatingItem) {
      this.selectedRatingItem.userRating = rating;
      console.log(`Node ${this.selectedRatingItem.id} rated ${rating}/5 stars`);
      this.closeRatingModal();
    }
  }
}