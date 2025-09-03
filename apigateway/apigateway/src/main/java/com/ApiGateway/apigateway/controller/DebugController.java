package com.ApiGateway.apigateway.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ApiGateway.apigateway.utils.JwtUtil;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/debug")
public class DebugController {

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/token")
    public ResponseEntity<?> checkToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Invalid Authorization header");
        }

        String token = authHeader.substring(7);
        Map<String, Object> response = new HashMap<>();
        
        try {
            response.put("valid", jwtUtil.validateToken(token));
            response.put("username", jwtUtil.extractUsername(token));
            response.put("role", jwtUtil.extractRole(token));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}