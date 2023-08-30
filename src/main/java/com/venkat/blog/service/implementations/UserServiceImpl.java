package com.venkat.blog.service.implementations;

import com.venkat.blog.model.User;
import com.venkat.blog.repository.UserRepository;
import com.venkat.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    UserRepository userRepository;
     PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public User findByName(String name) {

        return userRepository.findByName(name);
    }


    @Override
    public void registerUser(User user) {
        String bcryptPassword = passwordEncoder.encode(user.getPassword());

        user.setPassword(bcryptPassword);
        userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {

        return userRepository.findByEmail(email);
    }

}
