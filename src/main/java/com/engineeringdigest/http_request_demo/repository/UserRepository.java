package com.engineeringdigest.http_request_demo.repository;


import com.engineeringdigest.http_request_demo.model.User;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByName(String name);

    List<User> findByAgeGreaterThan(int age);
    // SELECT * FROM users WHERE age > ?

    List<User> findByNameContainingIgnoreCase(String name);
    // SELECT * FROM users WHERE LOWER(name) LIKE LOWER(CONCAT('%', ?, '%'))

    boolean existsByName(String name);
    // SELECT COUNT(*) > 0 FROM users WHERE name = ?

    List<User> findByAgeBetweenOrderByAgeAsc(int min, int max);
    // SELECT * FROM users WHERE age BETWEEN ? AND ? ORDER BY age ASC

    long countByAgeGreaterThanEqual(int age);

    List<User> findByNameContainingIgnoreCaseAndAgeGreaterThan(String name, int age);
}
