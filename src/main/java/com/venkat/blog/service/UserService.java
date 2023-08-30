package com.venkat.blog.service;

import com.venkat.blog.model.User;

import java.util.List;

public interface UserService {
    User findByName(String name);
    void registerUser(User user);
    User findByEmail(String email);
}
