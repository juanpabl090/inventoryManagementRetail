package com.example.inventoryManagementRetail.service;

import com.example.inventoryManagementRetail.dto.User.Register.RegisterRequest;
import com.example.inventoryManagementRetail.dto.User.Register.RegisterResponse;
import com.example.inventoryManagementRetail.dto.User.login.AuthenticationRequestDto;
import com.example.inventoryManagementRetail.dto.User.login.AuthenticationResponse;
import com.example.inventoryManagementRetail.model.User;
import com.example.inventoryManagementRetail.repository.UserRepository;
import com.example.inventoryManagementRetail.security.Jwt.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtils jwtUtils;

    public AuthService(PasswordEncoder passwordEncoder, UserRepository userRepository, AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService, JwtUtils jwtUtils) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
    }

    public AuthenticationResponse login(AuthenticationRequestDto request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword())
            );
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUserName());
            String jwt = jwtUtils.generateToken(userDetails);
            return new AuthenticationResponse(jwt);
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid credentials");
        }
    }

    public RegisterResponse regiseter(RegisterRequest request) {
        try {
            if (userRepository.findByUserName(request.getUserName()).isPresent()) {
                throw new IllegalArgumentException("User Already Exists");
            }
            String passwordEncoded = passwordEncoder.encode(request.getPassword());
            User user = User.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .userName(request.getUserName())
                    .password(passwordEncoded)
                    .roles(request.getRoles())
                    .build();
            user = userRepository.save(user);
            log.info("User registered successfully: {}", user.getUserName());
            return RegisterResponse.builder()
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .userName(user.getUserName())
                    .roles(user.getRoles())
                    .build();
        } catch (Exception e) {
            log.error("Error while registering user: {}", e.getMessage());
            throw new RuntimeException("Error while registering user: " + e.getMessage());
        }
    }

}