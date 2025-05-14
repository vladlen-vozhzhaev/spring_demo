package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class UserController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/profile")
    public String showUserProfile(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null){
            model.addAttribute("username", auth.getName());
        }
        return "profile";
    }
    @PostMapping("/change-password")
    public String changePassword(@RequestParam String password, Principal principal){
        User user = userRepository.findByUsername(principal.getName());
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return "redirect:/profile";
    }
}
