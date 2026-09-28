package com.cinema.rating;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@SpringBootApplication
public class RatingApplication {

    public static void main(String[] args) {
        ensureDatabaseExists();
        SpringApplication.run(RatingApplication.class, args);
    }

    private static void ensureDatabaseExists() {
        String dbUrl = System.getenv("SPRING_DATASOURCE_URL");
        if (dbUrl == null || dbUrl.isBlank()) {
            dbUrl = "jdbc:postgresql://localhost:5432/rating_db";
        }

        String username = System.getenv().getOrDefault("SPRING_DATASOURCE_USERNAME", "pgadmin");
        String password = System.getenv().getOrDefault("SPRING_DATASOURCE_PASSWORD", "pgpass");

        try {
            int lastSlashIndex = dbUrl.lastIndexOf('/');
            if (lastSlashIndex == -1) return;

            String dbName = dbUrl.substring(lastSlashIndex + 1);
            if (dbName.contains("?")) {
                dbName = dbName.substring(0, dbName.indexOf('?'));
            }

            String rootUrl = dbUrl.substring(0, lastSlashIndex) + "/postgres";

            try (Connection conn = DriverManager.getConnection(rootUrl, username, password);
                 Statement stmt = conn.createStatement()) {

                ResultSet rs = stmt.executeQuery("SELECT 1 FROM pg_database WHERE datname = '" + dbName + "'");
                if (!rs.next()) {
                    stmt.executeUpdate("CREATE DATABASE " + dbName);
                    System.out.println("Database '" + dbName + "' created successfully.");
                }
            }
        } catch (Exception e) {
            System.err.println("Database creation check warning: " + e.getMessage());
        }
    }
}