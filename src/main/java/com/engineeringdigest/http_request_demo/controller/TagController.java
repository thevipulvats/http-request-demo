package com.engineeringdigest.http_request_demo.controller;

import com.engineeringdigest.http_request_demo.exception.ResourceNotFoundException;
import com.engineeringdigest.http_request_demo.model.Post;
import com.engineeringdigest.http_request_demo.model.Tag;
import com.engineeringdigest.http_request_demo.repository.PostRepository;
import com.engineeringdigest.http_request_demo.repository.TagRepository;
import com.engineeringdigest.http_request_demo.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @PostMapping("/{postId}")
    public Post addTagsToPost(@PathVariable int postId,
                              @RequestBody List<String> tagNames) {
        return tagService.addTagsToPost(postId, tagNames);
    }
}
