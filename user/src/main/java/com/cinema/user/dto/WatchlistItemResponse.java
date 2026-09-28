package com.cinema.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WatchlistItemResponse {
    private Long id;
    private Long userId;
    private Long movieId;
    private LocalDateTime addedAt;
}