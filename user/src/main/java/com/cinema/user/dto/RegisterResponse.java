package com.cinema.user.dto;

import lombok.Data;

@Data
public class RegisterResponse {

    private String email;
    private String otpAuthUri;

    public RegisterResponse(String email, String otpAuthUri) {
        this.email = email;
        this.otpAuthUri = otpAuthUri;
    }
}