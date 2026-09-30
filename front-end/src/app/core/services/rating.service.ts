import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
// import { environment } from '../../../environments/environment';
import { environment } from '../../../environments/environment.development';
import { RatingRequest, RatingResponse } from '../../models/rating.model';

@Injectable({ providedIn: 'root' })
export class RatingService {
  private readonly apiUrl = `${environment.apiBaseUrl}/ratings`;

  constructor(private readonly http: HttpClient) {}

  submitRating(request: RatingRequest): Observable<RatingResponse> {
    return this.http.post<RatingResponse>(this.apiUrl, request);
  }
}
