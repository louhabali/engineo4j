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
  id: number;
  title: string;
  year: number;
  rating: number;
  genre: string;
  posterUrl: string;
}
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