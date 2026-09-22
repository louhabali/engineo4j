
## Setup

### Steps

1. **Dependencies** — Add Spring Web, JPA, Security, PostgreSQL, Redis, JJWT, and TOTP dependencies to `pom.xml`.
2. **Configuration** — Configure PostgreSQL and Redis in `application.yml`.
3. **User Entity** — Create the `User` entity with credentials, `mfaEnabled`, and `totpSecret`.
4. **TOTP Service** — Generate TOTP secrets, QR code URIs, and validate 6-digit codes.
5. **Authentication** — Implement registration, login, TOTP verification, and JWT token issuance.

## Authentication Flow

```text
Register
   ↓
Generate QR Code
   ↓
Scan with Google Authenticator
   ↓
Login with Password
   ↓
Enter 6-Digit Code
   ↓
Receive JWT
```

## Architectural Decisions

* **TOTP + Google Authenticator:** Time-based 2FA that works offline and avoids SMS-based authentication.
* **PostgreSQL:** Reliable relational database with ACID transactions and structured user data.
