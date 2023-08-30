package com.venkat.blog.service;

import com.venkat.blog.model.Comment;
import com.venkat.blog.model.Tag;

import java.util.List;

public interface CommentService {
    Comment findById(Integer id);
    void save(Comment comment);
    void deleteById(Integer id);
    List<Comment> findAllBYPostId(Integer postId);
}
