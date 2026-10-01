import { MovieCard } from './movie.model';

export interface GraphRecommendation extends MovieCard {
  relevanceScore: number;
  userRating?: number;
}