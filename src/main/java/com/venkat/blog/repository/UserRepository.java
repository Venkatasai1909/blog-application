package com.venkat.blog.repository;

import com.venkat.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByName(String name);
    User findByEmail(String email);
}
