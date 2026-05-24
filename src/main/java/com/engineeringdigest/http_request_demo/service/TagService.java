package com.engineeringdigest.http_request_demo.service;

import com.engineeringdigest.http_request_demo.exception.ResourceNotFoundException;
import com.engineeringdigest.http_request_demo.model.Post;
import com.engineeringdigest.http_request_demo.model.Tag;
import com.engineeringdigest.http_request_demo.repository.PostRepository;
import com.engineeringdigest.http_request_demo.repository.TagRepository;
import com.engineeringdigest.http_request_demo.security.AccessControl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Slf4j
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final PostRepository postRepository;
    private final AccessControl accessControl;

    public Post addTagsToPost(int postId, List<String> tagNames) {
        log.info("Adding tags to postId: {}", postId);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        accessControl.allowSameUserOrAdmin(post.getUser().getId());
        for (String tagName : tagNames) {
            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> {
                        Tag newTag = new Tag();
                        newTag.setName(tagName);
                        return tagRepository.save(newTag);
                    });
            post.getTags().add(tag);
        }
        return postRepository.save(post);
    }
}
