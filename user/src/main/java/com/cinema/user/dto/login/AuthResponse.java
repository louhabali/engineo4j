package com.cinema.user.dto.login;

import lombok.Data;

@Data
public class AuthResponse {

    private String token;
    private String tokenType;

    public AuthResponse(String token) {
        this.token = token;
        this.tokenType = "Bearer";
    }
}