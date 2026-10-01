import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { RatingRequest, RatingResponse, RatingSummary } from '../../models/rating.model';

@Injectable({ providedIn: 'root' })
export class RatingService {
  private readonly apiUrl = `${environment.apiBaseUrl}/ratings`;

  constructor(private readonly http: HttpClient) {}

  submitRating(request: RatingRequest): Observable<RatingResponse> {
    return this.http.post<RatingResponse>(this.apiUrl, request);
  }

  getMovieRatingSummary(movieId: string | number): Observable<RatingSummary> {
    return this.http.get<RatingSummary>(`${this.apiUrl}/movie/${movieId}/summary`);
  }

  getMovieRatingSummaries(movieIds: Array<string | number>): Observable<RatingSummary[]> {
    let params = new HttpParams();
    for (const movieId of movieIds) {
      params = params.append('movieIds', String(movieId));
    }
    return this.http.get<RatingSummary[]>(`${this.apiUrl}/summaries`, { params });
  }
}
