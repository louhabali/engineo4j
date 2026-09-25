package com.cinema.rating.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ratings")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private Long movieId;
    private int score; // 1 to 5
}