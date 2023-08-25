package com.venkat.blog.service.implementations;

import com.venkat.blog.model.User;
import com.venkat.blog.repository.UserRepository;
import com.venkat.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public User findById(Integer id) {
        return null;
    }

    @Override
    public void save(User user) {

    }

    @Override
    public void deleteById(Integer id) {

    }
}
