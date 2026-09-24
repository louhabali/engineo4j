package com.cinema.user.controller;

import com.cinema.user.dto.AuthResponse;
import com.cinema.user.dto.MfaLoginRequest;
import com.cinema.user.dto.MfaVerifyRequest;
import com.cinema.user.dto.RegisterRequest;
import com.cinema.user.dto.RegisterResponse;
import com.cinema.user.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.Data;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Data
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponse register(
            @Valid @RequestBody RegisterRequest request) {
        System.out.println("[REQUEST IN REGISTER] : " + request.toString());
        return authService.register(request);
    }

    @PostMapping("/mfa/verify")
    public ResponseEntity<Void> verifyMfa(
            @Valid @RequestBody MfaVerifyRequest request) {
        authService.verifyMfa(request);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public AuthResponse login(
            @Valid @RequestBody MfaLoginRequest request) {
        return authService.login(request);
    }
}