export interface FeaturedMovie {
  id: number;
  title: string;
  tagline: string;
  description: string;
  rating: number;
  year: number;
  matchPercentage: number;
  bannerUrl: string;
}

export interface MovieCard {
  id: number | string; 
  movieId?: string;
  title: string;
  releaseYear: number;   
  averageRating: number; 
  genres: string[];    
  posterUrl: string;
  bannerUrl?: string;
}

export interface MovieDetail {
  id: number | string;
  movieId?: string;
  title: string;
  tagline: string;
  synopsis?: string;    
  description?: string;
  averageRating: number;
  releaseYear: number;
  duration: string;
  director: string;
  genres: string[];
  bannerUrl: string;
  posterUrl: string;
  userRating?: number;
  inwatchlist?: boolean;
}

export interface RelatedGraphMovie {
  id: number | string;
  title: string;
  posterUrl: string;
  affinityScore: number;
  connectionReason: string;
}