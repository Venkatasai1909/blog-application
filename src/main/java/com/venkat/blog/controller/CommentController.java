package com.venkat.blog.controller;

import com.venkat.blog.model.Comment;
import com.venkat.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
public class CommentController {
    CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/save-comment")
    public String saveComment(@RequestParam("postId") Integer postId, @ModelAttribute("comment") Comment comment) {
        if (comment.getCreatedAt() == null) {
            comment.setCreatedAt(LocalDateTime.now());
        }
        comment.setPostId(postId);
        commentService.save(comment);

        return "redirect:/post/" + postId;
    }

    @PostMapping("/update-comment")
    public String updateCommentForm(@ModelAttribute("commentId") Integer commentId, Model model) {
        Comment comment = commentService.findById(commentId);
        model.addAttribute("comment", comment);

        return "update-comment-form";
    }

    @PostMapping("/delete-comment")
    public String deleteComment(@RequestParam("commentId") Integer commentId, @RequestParam("postId") Long postId) {
        commentService.deleteById(commentId);
        return "redirect:/post/" + postId;
    }

}
