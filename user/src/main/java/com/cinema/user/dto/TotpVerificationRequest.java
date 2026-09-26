package com.cinema.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data

public class TotpVerificationRequest {

    @NotBlank
    private String email;

    @NotBlank
    @Pattern(regexp = "\\d{6}", message = "TOTP code must contain exactly 6 digits")
    private String code;

}
