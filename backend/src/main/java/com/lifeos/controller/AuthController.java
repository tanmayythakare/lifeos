package com.lifeos.controller;

import com.lifeos.dto.AuthResponse;
import com.lifeos.dto.LoginRequest;
import com.lifeos.dto.RegisterRequest;
import com.lifeos.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        String token = authService.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(new AuthResponse(token, "Login Successful"));
    }
}
