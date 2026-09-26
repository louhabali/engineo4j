package com.cinema.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cinema.user.models.*;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    
    boolean existsByEmail(String email);

    boolean existsByFullName(String fullName);
}
