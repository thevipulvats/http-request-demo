package com.engineeringdigest.http_request_demo.service;

import com.engineeringdigest.http_request_demo.exception.ResourceNotFoundException;
import com.engineeringdigest.http_request_demo.model.Post;
import com.engineeringdigest.http_request_demo.model.User;
import com.engineeringdigest.http_request_demo.repository.PostRepository;
import com.engineeringdigest.http_request_demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Post createPost(int userId, String title, String content) {
        log.info("Creating post for userId: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setUser(user);
        return postRepository.save(post);
    }

    public List<Post> getPostsByUser(int userId) {
        log.info("Fetching posts for userId: {}", userId);
        return postRepository.findByUserId(userId);
    }
}
