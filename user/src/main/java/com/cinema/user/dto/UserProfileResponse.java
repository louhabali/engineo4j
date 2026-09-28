package com.cinema.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {
    private String fullName;
    private String email;
    private String memberSince;
    private long totalWatchlist;
    private long totalFavorites;
    private long totalRatings;
}