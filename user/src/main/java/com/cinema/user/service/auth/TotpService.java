package com.cinema.user.service.auth;

import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.code.DefaultCodeGenerator;
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
        // this.codeVerifier = new DefaultCodeVerifier(new DefaultCodeGenerator());
        // this.codeVerifier = new DefaultCodeVerifier(new DefaultCodeGenerator(),
        // null);
        this.codeVerifier = new DefaultCodeVerifier(new DefaultCodeGenerator(), new SystemTimeProvider());
    }

    /**
     * Generates a new secret for a user.
     */
    public String generateSecret() {
        return secretGenerator.generate();
    }

    /**
     * Builds the URI understood by Google Authenticator
     * and other authenticator applications.
     */
    public String generateOtpAuthUri(String email, String secret) {

        String issuer = "UserService";

        return String.format(
                "otpauth://totp/%s:%s?secret=%s&issuer=%s",
                issuer,
                email,
                secret,
                issuer);
    }

    /**
     * Validates a 6-digit TOTP code.
     */
    public boolean verifyCode(String secret, String code) {
        return codeVerifier.isValidCode(secret, code);
    }

    // public QrData generateQrData(String email, String secret) {
    // return new QrData.Builder()
    // .label(email)
    // .secret(secret)
    // .issuer("User Service")
    // .build();
    // }
}
