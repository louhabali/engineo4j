package com.cinema.user.dto.watch_list;

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