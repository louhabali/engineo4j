package com.cinema.user.service;

import com.cinema.user.dto.UserProfileResponse;
import com.cinema.user.dto.UserProfileUpdateRequest;
import com.cinema.user.models.User;
import com.cinema.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserProfileResponse getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Note: Replace total counts with calls to your Watchlist/Favorites/Ratings repos or services if needed
        return UserProfileResponse.builder()
                .fullName(user.getFullName())
                .email(user.getEmail())
                .memberSince("2026") // Can be extracted from a createdAt field if added to User entity
                .totalWatchlist(0)
                .totalFavorites(0)
                .totalRatings(0)
                .build();
    }

    @Transactional
    public UserProfileResponse updateUserProfile(Long userId, UserProfileUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());

        User updatedUser = userRepository.save(user);

        return UserProfileResponse.builder()
                .fullName(updatedUser.getFullName())
                .email(updatedUser.getEmail())
                .memberSince("2026")
                .totalWatchlist(0)
                .totalFavorites(0)
                .totalRatings(0)
                .build();
    }
}