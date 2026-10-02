package com.cinema.user.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "movie_shares")
@Getter
@Setter
@NoArgsConstructor
public class MovieShare {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "movie_id", nullable = false)
    private Long movieId;

    @Column(name = "sender_user_id", nullable = false)
    private Long senderUserId;

    @Column(name = "recipient_user_id")
    private Long recipientUserId;

    @Column(name = "friend_email", nullable = false)
    private String friendEmail;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public MovieShare(Long movieId, Long senderUserId, Long recipientUserId, String friendEmail) {
        this.movieId = movieId;
        this.senderUserId = senderUserId;
        this.recipientUserId = recipientUserId;
        this.friendEmail = friendEmail;
    }

    @PrePersist
    void setCreatedAt() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}