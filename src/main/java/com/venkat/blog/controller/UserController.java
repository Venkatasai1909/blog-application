package com.venkat.blog.controller;

import com.venkat.blog.model.User;
import com.venkat.blog.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {
    UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @RequestMapping("/register")
    public String register(Model model){
        User user = new User();
        model.addAttribute("user", user);

        return "register";
    }

    @PostMapping("/register-user")
    public String registerUser(@ModelAttribute("user") User user,
                               @RequestParam("confirm_password") String password,Model model) {
        if(userService.findByName(user.getName()) != null  || userService.findByEmail(user.getEmail()) != null) {
            model.addAttribute("error", "User already exists...");

            return "register";
        }
        System.out.println(user.getName());
        if(!user.getPassword().equals(password)) {

            return "register";
        }
        userService.registerUser(user);

        return "redirect:/";
    }

    @GetMapping("/login")
    public String loginUser() {
        return "login";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }
}
