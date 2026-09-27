package com.cinema.user.service.auth;

import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import org.springframework.stereotype.Service;

@Service
public class TotpService {

    private final SecretGenerator secretGenerator;
    private final CodeVerifier codeVerifier;

    public TotpService() {
        this.secretGenerator = new DefaultSecretGenerator();
        this.codeVerifier = new DefaultCodeVerifier(new DefaultCodeGenerator(), new SystemTimeProvider());
    }

    /**
     * Generates a new secret for a user.
     */
    public String generateSecret() {
        return secretGenerator.generate();
    }

    /**
     * Builds a fully compliant otpauth URI using the library's QrData builder.
     */
    public String generateOtpAuthUri(String email, String secret) {
        QrData data = new QrData.Builder()
                .label(email)
                .secret(secret)
                .issuer("UserService")
                .algorithm(HashingAlgorithm.SHA1) // Google Authenticator requires SHA1
                .digits(6)                        // Standard 6-digit codes
                .period(30)                       // Refreshes every 30 seconds
                .build();

        return data.getUri();
    }

    /**
     * Validates a 6-digit TOTP code.
     */
    public boolean verifyCode(String secret, String code) {
        return codeVerifier.isValidCode(secret, code);
    }
}