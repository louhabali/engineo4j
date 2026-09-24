package com.cinema.user.service.auth;

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
}
