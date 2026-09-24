package com.cinema.user.service.auth;

import com.cinema.user.dto.AuthResponse;
import com.cinema.user.dto.LoginRequest;
import com.cinema.user.dto.MfaLoginRequest;
import com.cinema.user.dto.MfaLoginResponse;
import com.cinema.user.dto.MfaVerifyRequest;
import com.cinema.user.dto.RegisterRequest;
import com.cinema.user.dto.RegisterResponse;
import com.cinema.user.models.User;
import com.cinema.user.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.Data;

import org.springframework.dao.DuplicateKeyException;
// import com.cinema.user.exceptions.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Data
@AllArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TotpService totpService;
    private final JwtService jwtService;

    @Transactional
    public RegisterResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateKeyException("Email already registered");
        }

        String totpSecret = totpService.generateSecret();

        User user = new User();

        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        // user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setTotpSecret(totpSecret);
        user.setMfaEnabled(false);

        userRepository.save(user);

        String otpAuthUri = totpService.generateOtpAuthUri(
                user.getEmail(),
                totpSecret);

        return new RegisterResponse(
                user.getEmail(),
                otpAuthUri);
    }

    @Transactional
    public void verifyMfa(MfaVerifyRequest request) {

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.isMfaEnabled()) {
            throw new IllegalStateException("MFA is already enabled");
        }

        boolean valid = totpService.verifyCode(
                user.getTotpSecret(),
                request.code());

        if (!valid) {
            throw new IllegalArgumentException("Invalid verification code");
        }

        user.setMfaEnabled(true);

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(MfaLoginRequest request) {
        System.out.println("[LOGIN REQUEST] ===> " + request.toString());
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(
                request.password(),
                user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        if (!user.isMfaEnabled()) {
            throw new IllegalStateException("MFA is not enabled");
        }

        boolean validCode = totpService.verifyCode(
                user.getTotpSecret(),
                request.code());

        if (!validCode) {
            throw new IllegalArgumentException("Invalid MFA code");
        }

        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponse(token);
    }
}
