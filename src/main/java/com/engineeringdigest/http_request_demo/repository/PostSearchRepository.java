package com.engineeringdigest.http_request_demo.repository;

import com.engineeringdigest.http_request_demo.document.PostDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface PostSearchRepository extends ElasticsearchRepository<PostDocument, String> {

    List<PostDocument> findByTitle(String title);

    List<PostDocument> findByTitleOrContent(String title, String content);

    List<PostDocument> findByTags(String tag);
}
