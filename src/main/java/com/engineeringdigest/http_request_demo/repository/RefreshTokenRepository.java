package com.engineeringdigest.http_request_demo.repository;

import com.engineeringdigest.http_request_demo.model.RefreshToken;
import com.engineeringdigest.http_request_demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

    void deleteByUser(User user);
    Optional<RefreshToken> findByToken(String token);

}
