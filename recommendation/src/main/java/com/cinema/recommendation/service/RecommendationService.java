package com.cinema.recommendation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final Neo4jClient neo4jClient;

    public Collection<Map<String, Object>> getRecommendationsForUser(String userId) {
        String query = """
            MATCH (u:User {id: $userId})-[r1:RATED]->(m:Movie)<-[r2:RATED]-(other:User)
            WHERE r1.score > 4 AND r2.score > 4
            
            MATCH (other)-[r3:RATED]->(rec:Movie)
            WHERE r3.score > 4 AND NOT (u)-[:RATED]->(rec)
            
            RETURN DISTINCT rec.id AS movieId, count(*) AS relevanceScore
            ORDER BY relevanceScore DESC
            LIMIT 10
        """;

        return neo4jClient.query(query)
                .bindAll(Map.of("userId", userId))
                .fetch()
                .all();
    }
}