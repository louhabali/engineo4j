package com.cinema.user.controller;

import com.cinema.user.dto.UserProfileResponse;
import com.cinema.user.dto.UserProfileUpdateRequest;
import com.cinema.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getUserProfile(
            @RequestHeader("X-User-Id") Long userId) {
        UserProfileResponse response = userService.getUserProfile(userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserProfileResponse> updateUserProfile(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody UserProfileUpdateRequest request) {
        UserProfileResponse response = userService.updateUserProfile(userId, request);
        return ResponseEntity.ok(response);
    }
}