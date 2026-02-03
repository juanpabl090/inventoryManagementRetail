package com.example.inventoryManagementRetail.controller;

import com.example.inventoryManagementRetail.dto.User.Login.AuthenticationRequestDto;
import com.example.inventoryManagementRetail.dto.User.Login.AuthenticationResponse;
import com.example.inventoryManagementRetail.dto.User.Me.MeResponse;
import com.example.inventoryManagementRetail.dto.User.Register.RegisterRequest;
import com.example.inventoryManagementRetail.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequestDto request) {
        return authService.login(request);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @GetMapping("/me")
    public ResponseEntity<MeResponse> me() {
        return authService.me();
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refreshToken(@CookieValue(name = "refreshToken", required = false) String refreshToken) {
        return authService.refreshTokenExchange(refreshToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthenticationResponse> logout(@CookieValue(name = "refreshToken", required = false) String refreshToken) {
        return authService.logout(refreshToken);
    }
}