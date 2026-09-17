# DB Decision: PostgreSQL vs MongoDB (with Neo4j)

**Neo4j** handles all complex graph operations, movie recommendations, and user-relationship traversals. 

For standard CRUD microservices (**User**, **Movie**, and **Rating**), we selected **PostgreSQL** over **MongoDB**. Pairing PostgreSQL with Neo4j ensures strict data integrity, bulletproof security, and clean, maintainable Spring Boot code.

---

##  Data Integrity (The Movie Example)
In our app, a rating links a user to a movie. Keeping this relationship valid is critical before publishing events downstream to Kafka or Neo4j.

* **With MongoDB:** If a client submits a rating for a non-existent movie (`movie_id: 999`), MongoDB will blindly save it. Preventing this requires custom validation logic in Spring Boot before every write.
* **With PostgreSQL:** PostgreSQL uses native **Foreign Keys**. If `movie_id: 999` does not exist in the database, PostgreSQL instantly blocks the write, guaranteeing clean data out of the box.

---

##  Safer Authentication
User profiles, BCrypt hashed passwords, and 2FA keys require absolute stability. 
* **PostgreSQL** enforces strict **ACID compliance** out of the box, ensuring user account data never ends up partially saved or corrupted if a server crashes mid-request.

---

##  Cleaner Code
* **Spring Data JPA** and PostgreSQL are built to work together naturally, eliminating unnecessary BSON mapping code.
* Database schema updates are easily versioned and automated using standard migration tools like **Flyway**.

---

## Responsibilities
* **Neo4j:** Graph traversals, user similarity matching, and recommendation algorithms.
* **PostgreSQL:** Reliable, structured CRUD for Users, Movie catalog metadata, and Rating logs.