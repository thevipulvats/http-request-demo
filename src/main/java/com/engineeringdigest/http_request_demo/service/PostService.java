package com.engineeringdigest.http_request_demo.service;

import com.engineeringdigest.http_request_demo.document.PostDocument;
import com.engineeringdigest.http_request_demo.exception.ResourceNotFoundException;
import com.engineeringdigest.http_request_demo.model.Post;
import com.engineeringdigest.http_request_demo.model.Tag;
import com.engineeringdigest.http_request_demo.model.User;
import com.engineeringdigest.http_request_demo.repository.PostRepository;
import com.engineeringdigest.http_request_demo.repository.PostSearchRepository;
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
    private final PostSearchRepository postSearchRepository;

    public Post createPost(int userId, String title, String content) {
        log.info("Creating post for userId: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setUser(user);
        Post saved = postRepository.save(post);
        PostDocument postDocument = PostDocument.builder()
                .id(String.valueOf(saved.getId()))
                .title(saved.getTitle())
                .content(saved.getContent())
                .authorName(saved.getUser() != null ? saved.getUser().getName() : null)
                .tags(saved.getTags() != null ? saved.getTags().stream().map(Tag::getName).toList(): null)
                .createdAt(saved.getCreatedAt())
                .build();
        postSearchRepository.save(postDocument);
        log.info("Post indexed in Elasticsearch with id {}", postDocument.getId());
        return saved;
    }

    public List<PostDocument> searchPosts(String query){
        return postSearchRepository.findByTitleOrContent(query, query);
    }

    public List<PostDocument> searchByTag(String tag) {
        return postSearchRepository.findByTags(tag);
    }


    public List<Post> getPostsByUser(int userId) {
        log.info("Fetching posts for userId: {}", userId);
        return postRepository.findByUserId(userId);
    }
}
