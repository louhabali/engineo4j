export interface RatingRequest {
  movieId: string | number;
  score: number;
}

export interface RatingResponse {
  id: number;
  userId: string;
  movieId: string | number;
  score: number;
}

export interface RatingSummary {
  movieId: number;
  ratingCount: number;
  averageRating: number;
  userRating?: number | null;
}
