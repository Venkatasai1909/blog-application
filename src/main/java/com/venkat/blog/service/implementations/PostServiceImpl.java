package com.venkat.blog.service.implementations;

import com.venkat.blog.model.Post;
import com.venkat.blog.repository.PostRepository;
import com.venkat.blog.repository.TagRepository;
import com.venkat.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class PostServiceImpl implements PostService {
    PostRepository postRepository;
    TagRepository tagRepository;
    @Autowired
    public PostServiceImpl(PostRepository postRepository, TagRepository tagRepository) {
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
    }


    @Override
    public Page<Post> findAll(Pageable pageable) {
        return postRepository.findAllByPublished(pageable);
    }

    @Override
    public Post findById(Integer id) {
        return postRepository.findById(id).get();
    }

    @Override
    public void save(Post post) {
        postRepository.save(post);
    }

    @Override
    public void deleteById(Integer id) {
        postRepository.deleteById(id);
    }

    @Override
    public List<Post> findAllByDrafts() {
        return postRepository.findAllByDrafts();
    }

    @Override
    public Page<Post> findAllPostsBySearchRequest(String search, Pageable pageable) {
        return postRepository.findAllPostsBySearchRequest(search, pageable);
    }

    @Override
    public Set<String> findAllAuthors() {
        return postRepository.findAllDistinctAuthors();
    }

    @Override
    public Set<String> findTagNamesWithPublishedPosts() {
        return postRepository.findTagsWithPublishedPosts();
    }

    @Override
    public Page<Post> filterPosts(List<String> authorNames,
                                  List<String> tagNames, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        int authorList = authorNames == null ? 1:0;
        int tagList = tagNames == null ? 1:0;
        int dates = (startDate == null || endDate == null) ? 1:0;

        return  postRepository.filterPosts(authorList, tagList, dates,
                authorNames, tagNames, startDate, endDate, pageable);
    }

    @Override
    public Page<Post> filterAndSearchPosts(String searchRequest, List<String> authorNames, List<String> tagNames, LocalDateTime startDate,
                                           LocalDateTime endDate, Pageable pageable) {
        int authorList = authorNames == null ? 1:0;
        int tagList = tagNames == null ? 1:0;
        int startAndEndDate = (startDate == null || endDate == null) ? 1:0;

        return postRepository.filterAndSearchPosts(searchRequest, authorList, tagList, startAndEndDate,
                authorNames, tagNames, startDate, endDate,pageable);

    }

    @Override
    public Set<String> findDistinctAuthorsBySearchRequest(String searchRequest) {

        return postRepository.findDistinctAuthorsBySearchRequest(searchRequest);
    }

    @Override
    public Set<String> findDistinctTagsBySearchRequest(String searchRequest) {

        return postRepository.findDistinctTagsBySearchRequest(searchRequest);
    }


}