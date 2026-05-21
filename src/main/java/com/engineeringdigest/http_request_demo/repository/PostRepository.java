package com.engineeringdigest.http_request_demo.repository;

import com.engineeringdigest.http_request_demo.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    List<Post> findByUserId(int userId);

}