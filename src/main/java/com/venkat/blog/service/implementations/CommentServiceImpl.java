package com.venkat.blog.service.implementations;

import com.venkat.blog.model.Comment;
import com.venkat.blog.repository.CommentRepository;
import com.venkat.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CommentServiceImpl implements CommentService {
    CommentRepository commentRepository;
    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment findById(Integer id) {
        return commentRepository.findById(id).get();
    }

    @Override
    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    public void deleteById(Integer id) {
        commentRepository.deleteById(id);
    }

    @Override
    public List<Comment> findAllBYPostId(Integer postId) {
        List<Comment> comments = commentRepository.findAllByPostId(postId);
        return comments;
    }
}
