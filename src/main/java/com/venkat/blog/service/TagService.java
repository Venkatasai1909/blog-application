package com.venkat.blog.service;

import com.venkat.blog.model.Post;
import com.venkat.blog.model.Tag;
import com.venkat.blog.model.User;

import java.util.List;

public interface TagService {
    void save(Tag tag);
    Tag findByName(String tagName);


}
