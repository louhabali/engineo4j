# Neo4flix Remaining Work

This checklist tracks work identified by comparing the current code with the project brief. Items are ordered by priority and dependency. We can take them one at a time; check an item only when its code and behavior are complete.

## P0 — Make the services work together

- [x] **Align API paths across gateway and services.** Rating and Recommendation controllers now use the `/api/v1/...` paths already exposed by the gateway, so these routes need no path rewrite.
- [x] **Fix movie search year parameter.** Angular now sends `releaseYear`, matching the Movie service's request parameter.
- [x] **Create and configure service databases.** PostgreSQL Compose initialization creates `user_db`, `movie_db`, and `rating_db` for a fresh data volume. An existing volume requires a separate one-time database creation.
- [x] **Make frontend API URLs environment-aware.** Both Angular services use a shared API base URL and route through the gateway. Development uses `http://localhost:8089/api/v1`; production uses same-origin `/api/v1` and requires the deployment proxy to forward API requests to the gateway. Backend container DNS wiring is part of the Compose deployment task below.
- [x] **Deploy the application in Docker Compose.** Added container builds for all backend services and Angular, wired container DNS and runtime settings, added PostgreSQL/Redis/Kafka/Neo4j health checks, and configured Nginx to forward API requests to the gateway. Application services still lack their own readiness health checks.

## P1 — Complete the main user flows

- [ ] **Implement rating API support in Angular.** Add a rating service and send a user's selected score to the backend from movie details and recommendation cards.
- [ ] **Support one current rating per user and movie.** Define create/update behavior, persist rating timestamps, and prevent duplicate rows for repeated submissions.
- [ ] **Use the authenticated identity for ratings.** Remove the mock user fallback; ensure the gateway supplies a verified user ID and the Rating service requires it.
- [ ] **Load personalized recommendations in Angular.** Replace hard-coded sample cards with data from the Recommendation API and handle empty, loading, and error states.
- [ ] **Hydrate recommendation results with movie details.** Map Neo4j movie IDs to the catalog's movie records so the UI can display title, artwork, genres, year, and score.
- [ ] **Implement recommendation filtering.** Support the requested genre and release-date filters in the API and UI.
- [ ] **Finish rating history and CRUD.** Provide the user's ratings and the required update/delete behavior through the Rating API; show the user's ratings where appropriate.
- [ ] **Implement recommendation sharing.** Add a share flow for a recommendation and define what information or link is shared.

## P2 — Complete catalog, profile, and watchlist behavior

- [ ] **Implement Movie service write operations.** Add create, update, and delete operations with validation and suitable authorization if full CRUD remains required by the brief.
- [ ] **Complete profile summary data.** Replace the fixed membership year and zero counts with persisted or service-derived values; add a `createdAt` field if needed.
- [ ] **Resolve favorites behavior.** Implement favorites end to end, or remove the inactive favorites tab if it is outside the agreed scope.
- [ ] **Align watchlist client methods with the API.** Remove or implement the unused `/users/watchlist/items` call, and verify that IDs correspond to the Movie service's database IDs.
- [ ] **Replace generated placeholder catalog metadata.** Use accurate movie genres, years, directors, durations, and descriptions, or clearly identify the seed set as synthetic demo data.

## P3 — Security and production readiness

- [ ] **Enforce authorization at the service boundary.** Protect profile, watchlist, rating, and other private endpoints even if they are reached outside the gateway; ensure user IDs cannot be supplied or overridden by clients.
- [ ] **Define public endpoints consistently.** Align the gateway allowlist with actual controller routes and document which endpoints require authentication.
- [ ] **Add role-based access if needed.** Define roles and apply them to administrative or write operations; the current JWT flow does not establish role-based authorization.
- [ ] **Set and validate password policy.** Enforce the agreed minimum length and complexity during registration and password changes.
- [ ] **Remove development secrets from defaults.** Require environment-provided JWT, database, and Neo4j credentials for deployment.
- [ ] **Configure HTTPS for deployed traffic.** Terminate TLS at the chosen ingress or reverse proxy and serve the frontend and APIs over HTTPS.
- [ ] **Decide how logout/revocation works.** The gateway checks a Redis blacklist, but no logout endpoint currently adds tokens to it.
- [ ] **Avoid exposing sensitive authentication details.** Remove request logging of credentials or authentication payloads and replace `System.out` diagnostics with appropriately redacted logging.

## P4 — Verify and document

- [ ] **Add focused automated tests.** Cover auth/MFA, authorization, movie search, watchlist, rating create/update, Kafka-to-Neo4j updates, recommendation queries, and gateway routing.
- [ ] **Run backend and frontend builds and tests.** Fix compilation, configuration, and runtime issues found; record the commands and results.
- [ ] **Exercise the full Compose deployment.** Verify registration through MFA, login, catalog search, watchlist, rating submission, and personalized recommendations through the gateway.
- [ ] **Document local setup and deployment.** Include prerequisites, environment variables, startup steps, API routes, test commands, and known demo-data limitations.

## First item to tackle

Start with **P0.1: align API paths across gateway and services**. It is a small, concrete integration task and establishes the URL conventions needed by the frontend and later end-to-end work.
