import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map, switchMap } from 'rxjs';
import { MovieService } from './movie.service';
import { environment } from '../../../environments/environment.development';
import { GraphRecommendation } from '../../models/recommendations.model';

interface RecommendationRecord {
    movieId: number | string;
    relevanceScore: number;
}

@Injectable({ providedIn: 'root' })
export class RecommendationsService {
    private readonly http = inject(HttpClient);
    private readonly movieService = inject(MovieService);
    private readonly apiUrl = `${environment.apiBaseUrl}/recommendations`;

    getRecommendations(): Observable<GraphRecommendation[]> {
        console.log('[Fetching recommendations from API:]', this.http.get<RecommendationRecord[]>(this.apiUrl));
        return this.http.get<RecommendationRecord[]>(this.apiUrl).pipe(
            switchMap((records) => {
                console.log('[Fetched recommendation records:]', records);
                const validRecords = records.filter((record) => record.movieId != null);
                console.log('[Valid recommendation records:]', validRecords);
                const movieIds = validRecords.map((record) => Number(record.movieId));

                return this.movieService.getMoviesByIds(movieIds).pipe(
                    map((movies) => {
                        const moviesById = new Map(
                            movies.map((movie) => [String(movie.id), movie]),
                        );

                        return validRecords.flatMap((record) => {
                            const movie = moviesById.get(String(record.movieId));
                            return movie
                                ? [{ ...movie, relevanceScore: record.relevanceScore }]
                                : [];
                        });
                    }),
                );
            }),
        );
    }
}
