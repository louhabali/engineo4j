package com.cinema.user.controller;

import com.cinema.user.dto.ShareMovieRequest;
import com.cinema.user.dto.ShareMovieResponse;
import com.cinema.user.dto.ReceivedMovieShareResponse;
import com.cinema.user.service.MovieShareService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shares")
@RequiredArgsConstructor
public class MovieShareController {

    private final MovieShareService movieShareService;

    @PostMapping
    public ResponseEntity<ShareMovieResponse> shareMovie(
            @RequestHeader("X-User-Id") Long senderUserId,
            @Valid @RequestBody ShareMovieRequest request) {
        return ResponseEntity.ok(movieShareService.shareMovie(senderUserId, request));
    }

    @GetMapping("/received")
    public ResponseEntity<List<ReceivedMovieShareResponse>> getReceivedShares(
            @RequestHeader("X-User-Id") Long recipientUserId,
            @RequestHeader("X-User-Email") String recipientEmail) {
        return ResponseEntity.ok(movieShareService.getReceivedShares(recipientUserId, recipientEmail));
    }
}