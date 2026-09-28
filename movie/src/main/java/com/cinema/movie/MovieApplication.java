package com.cinema.movie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@SpringBootApplication
public class MovieApplication {

    public static void main(String[] args) {
        ensureDatabaseExists("movie_db");
        SpringApplication.run(MovieApplication.class, args);
    }

    private static void ensureDatabaseExists(String targetDb) {
        // Read host, port, user, pass from environment or fall back to local defaults
        String host = System.getenv().getOrDefault("POSTGRES_HOST", "localhost");
        String port = System.getenv().getOrDefault("POSTGRES_PORT", "5432");
        String user = System.getenv().getOrDefault("SPRING_DATASOURCE_USERNAME", "pgadmin");
        String pass = System.getenv().getOrDefault("SPRING_DATASOURCE_PASSWORD", "pgpass");

        String adminUrl = String.format("jdbc:postgresql://%s:%s/postgres", host, port);

        try (Connection conn = DriverManager.getConnection(adminUrl, user, pass);
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("SELECT 1 FROM pg_database WHERE datname = '" + targetDb + "'");

            if (!rs.next()) {
                stmt.executeUpdate("CREATE DATABASE " + targetDb);
                System.out.println(">>> [Movie-Service] Database '" + targetDb + "' created successfully!");
            } else {
                System.out.println(">>> [Movie-Service] Database '" + targetDb + "' already exists.");
            }

        } catch (Exception e) {
            System.err.println(">>> [Movie-Service] Warning during DB check/creation: " + e.getMessage());
        }
    }
}