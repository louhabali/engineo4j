package com.cinema.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@SpringBootApplication
public class UserApplication {

    public static void main(String[] args) {
        // Create the database if it doesn't exist BEFORE Spring Boot initializes the context
        ensureDatabaseExists("jdbc:postgresql://localhost:5432/postgres", "pgadmin", "pgpass", "user_db");
        
        SpringApplication.run(UserApplication.class, args);
    }

    private static void ensureDatabaseExists(String defaultUrl, String username, String password, String targetDb) {
        try (Connection connection = DriverManager.getConnection(defaultUrl, username, password);
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(
                "SELECT 1 FROM pg_database WHERE datname = '" + targetDb + "'"
            );

            if (!resultSet.next()) {
                statement.executeUpdate("CREATE DATABASE " + targetDb);
                System.out.println(">>> Database '" + targetDb + "' successfully created!");
            } else {
                System.out.println(">>> Database '" + targetDb + "' already exists.");
            }

        } catch (Exception e) {
            System.err.println(">>> Warning/Error while checking/creating database: " + e.getMessage());
        }
    }
}