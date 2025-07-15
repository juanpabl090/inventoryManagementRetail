package com.example.inventoryManagementRetail.controller;

import com.example.inventoryManagementRetail.dto.User.Register.RegisterRequest;
import com.example.inventoryManagementRetail.dto.User.Register.RegisterResponse;
import com.example.inventoryManagementRetail.dto.User.login.AuthenticationRequestDto;
import com.example.inventoryManagementRetail.dto.User.login.AuthenticationResponse;
import com.example.inventoryManagementRetail.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequestDto request) {
        AuthenticationResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        RegisterResponse response = authService.regiseter(request);
        return ResponseEntity.ok(response);
    }

}
