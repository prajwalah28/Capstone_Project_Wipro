package com.AuthService.authservice.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.AuthService.authservice.model.Role;
import com.AuthService.authservice.model.User;
import com.AuthService.authservice.repository.UserRepository;
import com.AuthService.authservice.security.JwtUtil;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public Map<String, String> register(User user) {
        // check if username already exists
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Username already taken!");
            return errorResponse;
        }

        // encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // save user
        userRepository.save(user);

        // success response
        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully!");
        return response;
    }

    public String login(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPassword())) {
        	Role role = userOpt.get().getRole();
        	Long userId = userOpt.get().getId(); // keep as enum
        	return jwtUtil.generateToken(username, role, userId); 
        }
        throw new RuntimeException("Invalid username or password");
    }
}
