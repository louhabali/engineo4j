import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FeaturedMovie, MovieCard, MovieDetail, RelatedGraphMovie } from '../../models/movie.model';

@Injectable({
  providedIn: 'root'
})
export class MovieService {
  private http = inject(HttpClient);
  private baseUrl = 'http://localhost:8082/api/v1/movies'; 
  
  getAllMovies(): Observable<MovieCard[]> {
    return this.http.get<MovieCard[]>(this.baseUrl);
  }
  
  getMovieById(id: string | number): Observable<MovieDetail> {
    return this.http.get<MovieDetail>(`${this.baseUrl}/${id}`);
  }

 

  getGraphRecommendations(id: string | number): Observable<RelatedGraphMovie[]> {
    return this.http.get<RelatedGraphMovie[]>(`${this.baseUrl}/${id}/recommendations`);
  }
  searchByTitle(title: string): Observable<MovieCard[]> {
    const params = new HttpParams().set('title', title);
    return this.http.get<MovieCard[]>(`${this.baseUrl}/search`, { params });
  }

  filterByGenre(genre: string): Observable<MovieCard[]> {
    const params = new HttpParams().set('genre', genre);
    return this.http.get<MovieCard[]>(`${this.baseUrl}/filter/genre`, { params });
  }

  filterByYear(year: number): Observable<MovieCard[]> {
    const params = new HttpParams().set('year', year);
    return this.http.get<MovieCard[]>(`${this.baseUrl}/filter/year`, { params });
  }
}