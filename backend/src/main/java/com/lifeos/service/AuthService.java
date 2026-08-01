package com.lifeos.service;

import com.lifeos.dto.AuthResponse;
import com.lifeos.dto.RegisterRequest;
import com.lifeos.exception.ConflictException;
import com.lifeos.model.User;
import com.lifeos.repository.UserRepository;
import com.lifeos.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       AuthenticationManager authenticationManager,
                       JwtUtil jwtUtil,
                       BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ConflictException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getUsername());
        return new AuthResponse(token, "Registration Successful");
    }

    public String login(String username, String password) {
        if (!userRepository.existsByUsername(username) && !userRepository.existsByEmail(username)) {
            throw new UsernameNotFoundException("User not found");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            User authenticatedUser = (User) authentication.getPrincipal();
            return jwtUtil.generateToken(authenticatedUser.getUsername());
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid credentials");
        }
    }
}
