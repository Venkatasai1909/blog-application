package com.venkat.blog.service;

import com.venkat.blog.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface PostService {
    Page<Post> findAll(Pageable pageable);
    Post findById(Integer id);
    void save(Post post);
    void deleteById(Integer id);
    List<Post> findAllByDrafts();
    Page<Post> findAllPostsBySearchRequest(String search, Pageable pageable);
    Set<String> findAllAuthors();
    Set<String> findTagNamesWithPublishedPosts();
    Page<Post> filterPosts(Set<String> authors,
                           Set<String> tagNames, LocalDateTime startDate, LocalDateTime endDate,  Pageable pageable);

    Page<Post> filterAndSearchPosts(String searchRequest,Set<String> authors, Set<String> tagNames, LocalDateTime startDate,
                                    LocalDateTime endDate, Pageable pageable);

    Set<String> findDistinctAuthorsBySearchRequest(String searchRequest);
    Set<String> findDistinctTagsBySearchRequest(String searchRequest);

}