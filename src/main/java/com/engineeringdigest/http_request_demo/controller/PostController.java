package com.engineeringdigest.http_request_demo.controller;

import com.engineeringdigest.http_request_demo.document.PostDocument;
import com.engineeringdigest.http_request_demo.exception.ResourceNotFoundException;
import com.engineeringdigest.http_request_demo.model.Post;
import com.engineeringdigest.http_request_demo.model.User;
import com.engineeringdigest.http_request_demo.repository.PostRepository;
import com.engineeringdigest.http_request_demo.repository.UserRepository;
import com.engineeringdigest.http_request_demo.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/search")
    public ResponseEntity<List<PostDocument>> searchPosts(@RequestParam String q) {
        return ResponseEntity.ok(postService.searchPosts(q));
    }

    @GetMapping("/search/tag")
    public ResponseEntity<List<PostDocument>> searchByTag(@RequestParam String tag) {
        return ResponseEntity.ok(postService.searchByTag(tag));
    }

}
