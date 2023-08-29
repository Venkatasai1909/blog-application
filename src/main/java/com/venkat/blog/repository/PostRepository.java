package com.venkat.blog.repository;

import com.venkat.blog.model.Post;
import com.venkat.blog.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query("SELECT post FROM Post post WHERE isPublished = true")
    Page<Post> findAllByPublished(Pageable pageable);


    @Query("SELECT post FROM Post post WHERE isPublished = false ORDER BY publishedAt DESC")
    List<Post> findAllByDrafts();

    @Query("SELECT DISTINCT post FROM Post post " +
            "LEFT JOIN post.tags tag " +
            "WHERE post.title iLIKE %:searchRequest% " +
            "OR post.content iLIKE %:searchRequest% " +
            "OR post.author iLIKE %:searchRequest% " +
            "OR tag.name iLIKE %:searchRequest% " +
            "AND post.isPublished=true")
    Page<Post> findAllPostsBySearchRequest(@Param("searchRequest") String searchRequest, Pageable pageable);


    @Query("SELECT DISTINCT author from Post WHERE isPublished=true")
    Set<String> findAllDistinctAuthors();

    @Query("SELECT DISTINCT t.name FROM Tag t JOIN t.posts p WHERE p.isPublished = true")
    Set<String> findTagsWithPublishedPosts();

    @Query("SELECT post FROM Post post WHERE post.isPublished = true " +
            "AND (:emptyAuthors = 1 OR post.author IN :authorNames)" +
            "AND (:emptyTags = 1 OR EXISTS(SELECT postTag FROM Post postTag JOIN postTag.tags tag WHERE postTag = post AND tag.name IN :tagNames))" +
            "AND (:emptyDates = 1 OR post.publishedAt BETWEEN :startDate AND :endDate)")
    Page<Post> filterPosts(@Param("emptyAuthors") int emptyAuthors, @Param("emptyTags") int emptyTags,
                           @Param("emptyDates") int emptyDates, @Param("authorNames") Set<String> authorNames,
                           @Param("tagNames") Set<String> tagNames, @Param("startDate") LocalDateTime startDate,
                           @Param("endDate") LocalDateTime endDate, Pageable pageable);


    @Query("SELECT DISTINCT post FROM Post post " +
            "LEFT JOIN post.tags tag " +
            "WHERE (post.title iLIKE %:searchRequest% " +
            "OR post.content iLIKE %:searchRequest% " +
            "OR post.author iLIKE %:searchRequest% " +
            "OR tag.name iLIKE %:searchRequest%) " +
            "AND (:emptyAuthors = 1 OR post.author IN :authorNames) " +
            "AND (:emptyTags = 1 OR EXISTS(SELECT postTag FROM Post postTag JOIN postTag.tags tag WHERE postTag = post AND tag.name IN :tagNames))" +
            "AND (:dates = 1 OR post.publishedAt BETWEEN :startDate AND :endDate) " +
            "AND post.isPublished = true")
    Page<Post> filterAndSearchPosts(@Param("searchRequest") String searchRequest,
                                    @Param("emptyAuthors") int emptyAuthors,
                                    @Param("emptyTags") int emptyTags,
                                    @Param("dates") int dates,
                                    @Param("authorNames") Set<String> authorNames,
                                    @Param("tagNames") Set<String> tagNames,
                                    @Param("startDate") LocalDateTime startDate,
                                    @Param("endDate") LocalDateTime endDate,
                                    Pageable pageable);

    @Query("SELECT DISTINCT post.author FROM Post post " +
            "WHERE (post.title LIKE %:searchRequest% " +
            "OR post.content LIKE %:searchRequest% " +
            "OR post.author LIKE %:searchRequest%) " +
            "AND post.isPublished=true")
    Set<String> findDistinctAuthorsBySearchRequest(@Param("searchRequest") String searchRequest);

    @Query("SELECT DISTINCT tag.name FROM Post post " +
            "LEFT JOIN post.tags tag " +
            "WHERE (post.title LIKE %:searchRequest% " +
            "OR post.content LIKE %:searchRequest% " +
            "OR post.author LIKE %:searchRequest%) " +
            "AND post.isPublished=true")
    Set<String> findDistinctTagsBySearchRequest(@Param("searchRequest") String searchRequest);

}