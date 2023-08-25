package com.venkat.blog.service;

import com.venkat.blog.model.Post;
import com.venkat.blog.model.Tag;
import com.venkat.blog.model.User;

import java.util.List;

public interface TagService {
    List<Tag> findAll();
    Tag findById(Integer id);
    void save(Tag tag);
    void deleteById(Integer id);
    Tag findByName(String tagName);


}
