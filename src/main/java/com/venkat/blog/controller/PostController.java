package com.venkat.blog.controller;

import com.venkat.blog.model.Comment;
import com.venkat.blog.model.Post;
import com.venkat.blog.model.Tag;
import com.venkat.blog.service.CommentService;
import com.venkat.blog.service.PostService;
import com.venkat.blog.service.TagService;
import com.venkat.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Controller
public class PostController {
    PostService postService;
    CommentService commentService;
    TagService tagService;
    UserService userService;
    @Autowired
    public PostController(PostService postService, CommentService commentService, TagService tagService,
                          UserService userService) {
        this.postService = postService;
        this.commentService = commentService;
        this.tagService = tagService;
        this.userService = userService;
    }

    @GetMapping("/new-post")
    public String addPost(Model model) {
        Post post = new Post();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthor = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_AUTHOR"));

        if (isAuthor) {
            String username = authentication.getName();
            post.setAuthor(username);
        }

        model.addAttribute("post", post);

        return "post-form";
    }

    @PostMapping("/save-post")
    public String savePost(@ModelAttribute("post") Post modelPost, @RequestParam("tagSet") String requestTags,
                           @RequestParam(value = "action") String action, Model model) {
        if(userService.findByName(modelPost.getAuthor()) == null) {
            model.addAttribute("error", "User doesn't exists...");

            return "post-form";
        }

        if(modelPost.getCreatedAt() == null) {
            modelPost.setCreatedAt(LocalDateTime.now());
        }

        String[] tagArray = requestTags.split(",");
        Set<Tag> tagSet = new HashSet<>();
        modelPost.setExcerpt(modelPost.getContent().substring(0, 20));

        for (String tagName : tagArray) {
            tagName = tagName.trim();
            Tag existingTag = tagService.findByName(tagName);
            if (existingTag != null) {
                tagSet.add(existingTag);
            } else {
                Tag newTag = new Tag();
                newTag.setName(tagName);
                newTag.setCreatedAt(LocalDateTime.now());
                tagService.save(newTag);

                tagSet.add(newTag);
            }
        }

        if(action.equals("Draft")) {
            modelPost.setPublished(false);
        }

        if(action.equals("Publish")) {
            modelPost.setPublished(true);
            modelPost.setPublishedAt(LocalDateTime.now());
        }

        if(action.equals("Update")) {
            modelPost.setUpdatedAt(LocalDateTime.now());
            modelPost.setPublished(true);
        }


        modelPost.setTags(tagSet);
        postService.save(modelPost);

        if(action.equals("Update Draft")) {
            modelPost.setUpdatedAt(LocalDateTime.now());
            modelPost.setPublished(false);
        }

        if(action.equals("Update Draft") || action.equals("Draft")) {
            return "redirect:/drafts";
        }

        return "redirect:/";
    }


    @GetMapping("/post/{id}")
    public String getPost(@PathVariable Long id, Model model) {
        Integer postId = id.intValue();
        List<Comment> comments = commentService.findAllBYPostId(postId);
        Post post = postService.findById(postId);

        Comment comment = new Comment();
        comment.setCreatedAt(LocalDateTime.now());
        model.addAttribute("comment", comment);

        model.addAttribute("post",post);
        model.addAttribute("comments",comments);

        return "post-page";

    }
    @PostMapping("/update-post")
    public String updatePost(@RequestParam("postId") Integer postId, Model model) {
        Post post = postService.findById(postId);
        Set<Tag> tags = post.getTags();
        StringBuilder tagsBuilder = new StringBuilder();
        boolean firstTag = true;

        for (Tag tag : tags) {
            if (!firstTag) {
                tagsBuilder.append(",");
            } else {
                firstTag = false;
            }
            tagsBuilder.append(tag.getName());
        }

        String postTags = tagsBuilder.toString();

        model.addAttribute("post", post);
        model.addAttribute("postTags", postTags);

        return "post-form";
    }

    @PostMapping("/delete-post")
    public String deletePost(@RequestParam("postId") Integer postId) {
        boolean isPublished = postService.findById(postId).isPublished();
        postService.deleteById(postId);

        if(isPublished == false) {
            return "redirect:/drafts";
        }

        return "redirect:/";
    }

    @GetMapping("/drafts")
    public String  drafts(Model model) {
        List<Post> posts = postService.findAllByDrafts();

        model.addAttribute("posts", posts);

        return "drafts";
    }

    @PostMapping("/publish")
    public String updatePublish(@RequestParam("postId") Integer postId) {
        Post post = postService.findById(postId);
        post.setPublishedAt(LocalDateTime.now());
        post.setPublished(true);

        postService.save(post);

        return "redirect:/";
    }

}


