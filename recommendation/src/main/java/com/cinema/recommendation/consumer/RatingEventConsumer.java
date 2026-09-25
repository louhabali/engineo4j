package com.cinema.recommendation.consumer;

import com.cinema.recommendation.event.MovieRatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class RatingEventConsumer {

    private final Neo4jClient neo4jClient;

    @KafkaListener(topics = "movie-ratings-topic", groupId = "recommendation-service-group")
    public void handleMovieRatedEvent(MovieRatedEvent event) {
        log.info("Received MovieRatedEvent from Kafka: user={}, movie={}, score={}", 
                event.userId(), event.movieId(), event.score());

        String query = """
            MERGE (u:User {id: $userId})
            MERGE (m:Movie {id: $movieId})
            MERGE (u)-[r:RATED]->(m)
            SET r.score = $score, r.updatedAt = timestamp()
        """;

       try {
            Map<String, Object> params = Map.of(
                "userId", event.userId(), 
                "movieId", event.movieId(), 
                "score", event.score()
            );

            neo4jClient.query(query)
                    .bindAll(params)
                    .run();
            log.info("Successfully updated Neo4j graph for user: {}", event.userId());
        } catch (Exception e) {
            log.error("Failed to update Neo4j graph for event: {}", e.getMessage(), e);
        }
    }
}   