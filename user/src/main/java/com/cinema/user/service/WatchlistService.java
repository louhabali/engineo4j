package com.cinema.user.service;

import com.cinema.user.models.Watchlist;
import com.cinema.user.repository.WatchlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WatchlistService {

    private final WatchlistRepository watchlistRepository;

    @Transactional(readOnly = true)
    public List<Long> getUserWatchlistMovieIds(Long userId) {
        return watchlistRepository.findByUserId(userId)
                .stream()
                .map(Watchlist::getMovieId)
                .toList();
    }

    @Transactional
    public void addToWatchlist(Long userId, Long movieId) {
        if (!watchlistRepository.existsByUserIdAndMovieId(userId, movieId)) {
            watchlistRepository.save(new Watchlist(userId, movieId));
        }
    }

    @Transactional
    public void removeFromWatchlist(Long userId, Long movieId) {
        watchlistRepository.deleteByUserIdAndMovieId(userId, movieId);
    }

    @Transactional(readOnly = true)
    public boolean isInWatchlist(Long userId, Long movieId) {
        return watchlistRepository.existsByUserIdAndMovieId(userId, movieId);
    }
}