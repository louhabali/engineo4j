import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  FeaturedMovie,
  MovieCard,
  MovieDetail,
  RelatedGraphMovie,
} from '../../models/movie.model';

@Injectable({
  providedIn: 'root',
})
export class MovieService {
  private http = inject(HttpClient);
  private baseUrl = 'http://localhost:8082/api/v1/movies';

  getAllMovies(): Observable<MovieCard[]> {
    return this.http.get<MovieCard[]>(this.baseUrl);
  }
  getPaginatedMovies(page: number, size: number): Observable<MovieCard[]> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<MovieCard[]>(`${this.baseUrl}/paginated`, { params });
  }
  getMovieById(id: string | number): Observable<MovieDetail> {
    return this.http.get<MovieDetail>(`${this.baseUrl}/${id}`);
  }

  getGraphRecommendations(
    id: string | number,
  ): Observable<RelatedGraphMovie[]> {
    return this.http.get<RelatedGraphMovie[]>(
      `${this.baseUrl}/${id}/recommendations`,
    );
  }
getFilteredMovies(
    query?: string,
    genre?: string,
    year?: number,
    page: number = 0,
    size: number = 8
  ): Observable<MovieCard[]> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (query && query.trim()) {
      params = params.set('query', query.trim());
    }
    if (genre && genre !== 'ALL') {
      params = params.set('genre', genre);
    }
    if (year) {
      params = params.set('year', year.toString());
    }

    return this.http.get<MovieCard[]>(`${this.baseUrl}/search`, { params });
  }


}
