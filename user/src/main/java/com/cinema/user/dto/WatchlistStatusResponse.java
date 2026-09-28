package com.cinema.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WatchlistStatusResponse {
    private Long movieId;
    private boolean inWatchlist;
}