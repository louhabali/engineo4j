package com.cinema.user.controller;

import com.cinema.user.service.WatchlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/watchlist")
@RequiredArgsConstructor
public class WatchlistController {

    private final WatchlistService watchlistService;

    @GetMapping
    public ResponseEntity<List<Long>> getWatchlistMovieIds(
            @RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(watchlistService.getUserWatchlistMovieIds(userId));
    }

    @PostMapping("/{movieId}")
    public ResponseEntity<Void> addToWatchlist(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long movieId) {
        watchlistService.addToWatchlist(userId, movieId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{movieId}")
    public ResponseEntity<Void> removeFromWatchlist(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long movieId) {
        watchlistService.removeFromWatchlist(userId, movieId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check/{movieId}")
    public ResponseEntity<Boolean> isInWatchlist(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long movieId) {
        return ResponseEntity.ok(watchlistService.isInWatchlist(userId, movieId));
    }
}