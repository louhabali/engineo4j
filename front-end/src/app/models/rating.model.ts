export interface RatingRequest {
  movieId: number;
  score: number;
}

export interface RatingResponse {
  id: number;
  userId: string;
  movieId: number;
  score: number;
}
