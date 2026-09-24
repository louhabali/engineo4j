package com.cinema.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


public record MfaVerifyRequest(

        @NotBlank @Email String email,

        @NotBlank @Pattern(regexp = "\\d{6}", message = "Code must contain exactly 6 digits") String code

) {
}
