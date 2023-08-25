package com.venkat.blog.controller;

import com.venkat.blog.model.Post;
import com.venkat.blog.model.Tag;
import com.venkat.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;


@Controller
public class HomeController {
    PostService postService;
    @Autowired
    public HomeController(PostService postService) {
        this.postService = postService;
    }


    @GetMapping("/")
    public String home(Model model) {
        return getListOfBlogs(1, "publishedAt", "desc", null,
                              null, null,null, null, model);
    }

    @GetMapping("/page/{pageNo}")
    public String getListOfBlogs(@PathVariable int pageNo, @RequestParam(defaultValue = "publishedAt") String sortField,
                                 @RequestParam(defaultValue = "desc") String sortDirection,
                                 @RequestParam(value = "search", required = false) String search,
                                 @RequestParam(value = "authors", required = false) List<String> selectedAuthors,
                                 @RequestParam(value = "tags", required = false) List<String> selectedTags,
                                 @RequestParam(value = "startDate", required = false) LocalDateTime startDate,
                                 @RequestParam(value = "endDate", required = false) LocalDateTime endDate,
                                 Model model) {

        int pageSize = 10;
        Sort sort = sortDirection.equals("desc") ? Sort.by(Sort.Order.desc(sortField)) : Sort.by(Sort.Order.asc(sortField));

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<Post> posts = null;

        if (search != null && !search.isEmpty()) {
            return "redirect:/search/page/" + pageNo + "?search=" + search;
        }

        if(selectedAuthors!=null || selectedTags!=null || startDate != null || endDate != null) {
            posts = postService.filterPosts(selectedAuthors, selectedTags, startDate, endDate, pageable);
        } else {
            posts = postService.findAll(pageable);
        }

        System.out.println(posts.getSize());

        List<String> authors = postService.findAllAuthors();
        List<String> tags = postService.findTagNamesWithPublishedPosts();

        model.addAttribute("posts", posts.getContent());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", posts.getTotalPages());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("authors", authors);
        model.addAttribute("tags", tags);

        return "blogs";
    }


    @GetMapping("/search/page/{pageNo}")
    public String searchBlogs(@PathVariable int pageNo,
                              @RequestParam("search") String search,
                              @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection,
                              @RequestParam(value = "sortField", defaultValue = "publishedAt") String sortField,
                              @RequestParam(value = "authors", required = false) List<String> selectedAuthors,
                              @RequestParam(value = "tags", required = false) List<String> selectedTags,
                              @RequestParam(value = "startDate", required = false) LocalDateTime startDate,
                              @RequestParam(value = "endDate", required = false) LocalDateTime endDate,
                              Model model) {
        int pageSize = 10;

        Sort sort;

        if (sortDirection.equals("desc")) {
            sort = Sort.by(Sort.Order.desc(sortField));
        } else {
            sort = Sort.by(Sort.Order.asc(sortField));
        }

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

        Page<Post> posts = null;

        if (selectedAuthors != null || selectedTags != null || startDate != null || endDate != null) {
            posts =postService.filterAndSearchPosts(search, selectedAuthors, selectedTags, startDate, endDate, pageable);
        } else {
            posts = postService.findAllPostsBySearchRequest(search, pageable);
        }

        Set<String> uniqueAuthors = new HashSet<>();

        List<Post> postListForAuthors = posts.getContent();
        for (Post post : postListForAuthors) {
            uniqueAuthors.add(post.getAuthor());
        }

        Set<String> uniqueTags = new HashSet<>();
        List<Post> postListForTags = posts.getContent();

        for (Post post :  postListForTags ) {
            for (Tag tag : post.getTags()) {
                uniqueTags.add(tag.getName());
            }
        }

        model.addAttribute("posts", posts.getContent());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", posts.getTotalPages());
        model.addAttribute("search", search);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("authors", uniqueAuthors);
        model.addAttribute("tags", uniqueTags);

        return "search-results";
    }

}
