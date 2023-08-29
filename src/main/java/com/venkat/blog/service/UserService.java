package com.venkat.blog.service;

import com.venkat.blog.model.User;

import java.util.List;

public interface UserService {
    List<User> findAll();
    User findByName(String name);
    void deleteById(Integer id);
    void registerUser(User user);
    User findByEmail(String email);
}
