package com.cinema.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record MfaLoginRequest(
        @NotBlank @Email String email,

        @NotBlank String password,

        @NotBlank @Pattern(regexp = "\\d{6}", message = "Code must contain exactly 6 digits") String code) {

}
