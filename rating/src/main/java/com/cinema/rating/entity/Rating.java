package com.cinema.rating.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;

@Entity
@Table(
    name = "ratings",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_ratings_user_movie",
        columnNames = {"user_id", "movie_id"}
    )
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "movie_id", nullable = false)
    private Long movieId;

    @Column(nullable = false)
    private int score; // 1 to 5

    @Column(
        name = "created_at",
        nullable = false,
        updatable = false,
        columnDefinition = "timestamp with time zone default CURRENT_TIMESTAMP"
    )
    private Instant createdAt;

    @Column(
        name = "updated_at",
        nullable = false,
        columnDefinition = "timestamp with time zone default CURRENT_TIMESTAMP"
    )
    private Instant updatedAt;

    @PrePersist
    void setCreationTimestamps() {
        Instant now = Instant.now();
        if (createdAt == null) {
            createdAt = now;
        }
        updatedAt = now;
    }

    @PreUpdate
    void setUpdateTimestamp() {
        updatedAt = Instant.now();
    }
}
