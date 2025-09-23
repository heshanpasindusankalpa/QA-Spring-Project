package com.example.QA.model;

import com.example.QA.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest_green {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private com.example.QA.controller.AuthController authController;

    public AuthControllerTest_green() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldPassIfUsernameIsNew() {
        User user = new User();
        user.setUsername("newUser");
        user.setPassword("password123");

        when(userRepository.findByUsername("newUser")).thenReturn(null);
        when(userRepository.save(user)).thenReturn(user);

        String response = authController.register(user);

        assertEquals("Registration successful", response);
    }

    @Test
    void shouldPassLoginWithCorrectPassword() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("correctPass");

        when(userRepository.findByUsername("testUser")).thenReturn(user);

        String response = authController.login(user);

        assertEquals("Login successful", response);
    }
}
