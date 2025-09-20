package com.example.QA.controller;

import com.example.QA.model.User;
import com.example.QA.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000") // Allow React app
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        System.out.println("Register request received for user: " + user.getUsername());

        if (userRepository.findByUsername(user.getUsername()) != null) {
            return "Username already exists";
        }

        userRepository.save(user);
        System.out.println("User registered successfully: " + user.getUsername());
        return "Registration successful";
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        System.out.println("Login request received for user: " + user.getUsername());

        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser == null || !existingUser.getPassword().equals(user.getPassword())) {
            System.out.println("Login failed for user: " + user.getUsername());
            return "Invalid username or password";
        }

        System.out.println("Login successful for user: " + user.getUsername());
        return "Login successful";
    }

    @GetMapping("/sample")
    public String sample() {
        return "Backend is running successfully!";
    }
}