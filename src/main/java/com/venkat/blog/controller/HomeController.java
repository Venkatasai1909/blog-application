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
    public String getListOfBlogs(@PathVariable Integer pageNo, @RequestParam(defaultValue = "publishedAt") String sortField,
                                 @RequestParam(defaultValue = "desc") String sortDirection,
                                 @RequestParam(value = "search", required = false) String search,
                                 @RequestParam(value = "selectedAuthors", required = false) Set<String> selectedAuthors,
                                 @RequestParam(value = "selectedTags", required = false) Set<String> selectedTags,
                                 @RequestParam(value = "startDate", required = false) LocalDateTime startDate,
                                 @RequestParam(value = "endDate", required = false) LocalDateTime endDate,
                                 Model model) {

        int pageSize = 10;
        Sort sort = sortDirection.equals("desc") ?
                                          Sort.by(Sort.Order.desc(sortField)) : Sort.by(Sort.Order.asc(sortField));

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<Post> posts = null;

        if (search != null && !search.isEmpty()) {
            return "redirect:/search/page/" + pageNo + "?search=" + search;
        }

        if((selectedAuthors!=null && !selectedAuthors.isEmpty()) || (selectedTags!=null && !selectedTags.isEmpty())
                || startDate != null || endDate != null) {
            posts = postService.filterPosts(selectedAuthors, selectedTags, startDate, endDate, pageable);
        } else {
            posts = postService.findAll(pageable);
        }

        Set<String> authors = postService.findAllAuthors();
        Set<String> tags = postService.findTagNamesWithPublishedPosts();

        model.addAttribute("posts", posts.getContent());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", posts.getTotalPages());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("selectedAuthors", selectedAuthors);
        model.addAttribute("selectedTags", selectedTags);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("authors", authors);
        model.addAttribute("tags", tags);

        return "blogs";
    }


    @GetMapping("/search/page/{pageNo}")
    public String searchBlogs(@PathVariable Integer pageNo,
                              @RequestParam("search") String search,
                              @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection,
                              @RequestParam(value = "sortField", defaultValue = "publishedAt") String sortField,
                              @RequestParam(value = "selectedAuthors", required = false) Set<String> selectedAuthors,
                              @RequestParam(value = "selectedTags", required = false) Set<String> selectedTags,
                              @RequestParam(value = "startDate", required = false) LocalDateTime startDate,
                              @RequestParam(value = "endDate", required = false) LocalDateTime endDate,
                              Model model) {
        int pageSize = 10;

        Sort sort = sortDirection.equals("desc") ?
                    Sort.by(Sort.Order.desc(sortField)) : Sort.by(Sort.Order.asc(sortField));


        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

        Page<Post> posts = null;
        if((selectedAuthors!=null && !selectedAuthors.isEmpty()) || (selectedTags!=null && !selectedTags.isEmpty())
                || startDate != null || endDate != null) {
            posts = posts = postService.filterAndSearchPosts(search, selectedAuthors, selectedTags,
                                                              startDate, endDate, pageable);
        } else {
            posts = postService.findAllPostsBySearchRequest(search, pageable);
        }

        Set<String> uniqueAuthors = postService.findDistinctAuthorsBySearchRequest(search);
        Set<String> uniqueTags = postService.findDistinctTagsBySearchRequest(search);

        model.addAttribute("posts", posts.getContent());
        model.addAttribute("totalPages", posts.getTotalPages());
        model.addAttribute("search", search);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("authors", uniqueAuthors);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("tags", uniqueTags);
        model.addAttribute("selectedAuthors", selectedAuthors);
        model.addAttribute("selectedTags", selectedTags);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "search-results";
    }

}