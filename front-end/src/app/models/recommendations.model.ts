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