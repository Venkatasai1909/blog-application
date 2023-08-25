package com.venkat.blog.service;

import com.venkat.blog.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;


import java.time.LocalDateTime;
import java.util.List;
public interface PostService {
    Page<Post> findAll(Pageable pageable);
    Post findById(Integer id);
    void save(Post post);
    void deleteById(Integer id);
    List<Post> findAllByDrafts();
    Page<Post> findAllPostsBySearchRequest(String search, Pageable pageable);
    List<String> findAllAuthors();
    List<String> findTagNamesWithPublishedPosts();
    Page<Post> filterPosts(List<String> authors,
                           List<String> tagNames, LocalDateTime startDate, LocalDateTime endDate,  Pageable pageable);

    Page<Post> filterAndSearchPosts(String searchRequest,List<String> authors, List<String> tagNames, LocalDateTime startDate,
                                    LocalDateTime endDate, Pageable pageable);

}
