package com.venkat.blog.service;

import com.venkat.blog.model.User;

import java.util.List;

public interface UserService {
    List<User> findAll();
    User findById(Integer id);
    void save(User user);
    void deleteById(Integer id);
}
