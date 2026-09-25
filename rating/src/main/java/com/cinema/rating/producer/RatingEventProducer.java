package com.cinema.rating.producer;

import com.cinema.rating.event.MovieRatedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RatingEventProducer {

    private final KafkaTemplate<String, MovieRatedEvent> kafkaTemplate;

    private static final String TOPIC = "movie-ratings-topic";

    public void sendRatingEvent(MovieRatedEvent event) {
        kafkaTemplate.send(TOPIC, event.userId(), event);
        log.info("Published MovieRatedEvent to Kafka topic '{}' for user: {} and movie: {}", 
                TOPIC, event.userId(), event.movieId());
    }
}