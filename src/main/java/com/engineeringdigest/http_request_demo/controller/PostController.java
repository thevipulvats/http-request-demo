package com.engineeringdigest.http_request_demo.controller;

import com.engineeringdigest.http_request_demo.exception.ResourceNotFoundException;
import com.engineeringdigest.http_request_demo.model.Post;
import com.engineeringdigest.http_request_demo.model.User;
import com.engineeringdigest.http_request_demo.repository.PostRepository;
import com.engineeringdigest.http_request_demo.repository.UserRepository;
import com.engineeringdigest.http_request_demo.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public Post createPost(@RequestParam int userId,
                           @RequestParam String title,
                           @RequestParam String content) {
        return postService.createPost(userId, title, content);
    }

    @GetMapping("/user/{userId}")
    public List<Post> getPostsByUser(@PathVariable int userId) {
        return postService.getPostsByUser(userId);
    }

}
