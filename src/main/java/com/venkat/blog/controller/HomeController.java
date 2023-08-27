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
                                 @RequestParam(value = "selectedAuthors", required = false) List<String> selectedAuthors,
                                 @RequestParam(value = "selectedTags", required = false) List<String> selectedTags,
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

        if((selectedAuthors!=null && !selectedAuthors.isEmpty()) || (selectedTags!=null && !selectedTags.isEmpty())
                || startDate != null || endDate != null) {
            System.out.println(selectedAuthors);
            System.out.println(selectedTags);
            posts = postService.filterPosts(selectedAuthors, selectedTags, startDate, endDate, pageable);
            System.out.println(posts.getContent());
            System.out.println("In filter page" + pageNo);
        } else {
            posts = postService.findAll(pageable);
            System.out.println("In Normal page" + pageNo);
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
                              @RequestParam(value = "selectedAuthors", required = false) List<String> selectedAuthors,
                              @RequestParam(value = "selectedTags", required = false) List<String> selectedTags,
                              @RequestParam(value = "startDate", required = false) LocalDateTime startDate,
                              @RequestParam(value = "endDate", required = false) LocalDateTime endDate,
                              Model model) {
        int pageSize = 10;

        Sort sort = sortDirection.equals("desc") ? Sort.by(Sort.Order.desc(sortField)) : Sort.by(Sort.Order.asc(sortField));


        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

        Page<Post> posts = null;
        if((selectedAuthors!=null && !selectedAuthors.isEmpty()) || (selectedTags!=null && !selectedTags.isEmpty())
                || startDate != null || endDate != null) {
            System.out.println(selectedAuthors);
            System.out.println(selectedTags);
            System.out.println("IN search and FIlter page");
            posts = posts = postService.filterAndSearchPosts(search, selectedAuthors, selectedTags, startDate, endDate, pageable);
        } else {
            System.out.println("In Search Page ");
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
//http://localhost:8080/page/2?search=&sortDirection=asc&sortField=publishedAt&selectedAuthors=&selectedTags=archaeology&selectedTags=art&selectedTags=artificial%20intelligence&selectedTags=astronomy&selectedTags=civilizations&selectedTags=coding&selectedTags=colonization&selectedTags=cooking&selectedTags=culinary&selectedTags=dreams&selectedTags=ethics&selectedTags=exploration&selectedTags=fair&selectedTags=fashion&selectedTags=fiction&selectedTags=food&selectedTags=future&selectedTags=galaxies&selectedTags=good&selectedTags=habits&selectedTags=heritage&selectedTags=history&selectedTags=innovation&selectedTags=jazz&selectedTags=light&selectedTags=Mediterranean&selectedTags=mindfulness&selectedTags=music&selectedTags=nature&selectedTags=open&selectedTags=photography&selectedTags=positive%20psychology&selectedTags=productivity&selectedTags=programming&selectedTags=psychology&selectedTags=read&selectedTags=recipes&selectedTags=renewable%20energy&selectedTags=self-care&selectedTags=self-improvement&selectedTags=space&selectedTags=space%20exploration&selectedTags=storytelling&startDate=&endDate=