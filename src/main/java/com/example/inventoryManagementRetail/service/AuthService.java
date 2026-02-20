package com.example.inventoryManagementRetail.service;

import com.example.inventoryManagementRetail.dto.User.Login.AuthenticationRequestDto;
import com.example.inventoryManagementRetail.dto.User.Login.AuthenticationResponse;
import com.example.inventoryManagementRetail.dto.User.Me.MeResponse;
import com.example.inventoryManagementRetail.dto.User.Register.RegisterRequest;
import com.example.inventoryManagementRetail.exception.DuplicateResourceException;
import com.example.inventoryManagementRetail.exception.RefreshTokenException;
import com.example.inventoryManagementRetail.exception.ResourceNotFoundException;
import com.example.inventoryManagementRetail.model.RefreshToken;
import com.example.inventoryManagementRetail.model.User;
import com.example.inventoryManagementRetail.repository.UserRepository;
import com.example.inventoryManagementRetail.security.Jwt.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtils jwtUtils;

    public AuthService(PasswordEncoder passwordEncoder, UserRepository userRepository, AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService, RefreshTokenService refreshTokenService, JwtUtils jwtUtils) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.refreshTokenService = refreshTokenService;
        this.jwtUtils = jwtUtils;
    }

    public ResponseEntity<AuthenticationResponse> login(AuthenticationRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword())
        );
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUserName());
        String jwt = jwtUtils.generateToken(userDetails);

        ResponseCookie cookie = ResponseCookie.from("token", jwt)
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(900)
                .build();

        User user = userRepository.findByUserName(request.getUserName()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        refreshTokenService.deleteByUser(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken.getToken())
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.between(Instant.now(), refreshToken.getExpiryDate()).getSeconds())
                .build();

        return ResponseEntity
                .ok()
                .headers(headers -> {
                    headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
                    headers.add(HttpHeaders.SET_COOKIE, refreshCookie.toString());
                })
                .body(new AuthenticationResponse("Login successfully"));
    }

    public ResponseEntity<AuthenticationResponse> refreshTokenExchange(String refreshTokenValue) {
        if (refreshTokenValue == null || refreshTokenValue.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthenticationResponse("refresh token missing"));
        }
        try {
            RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenValue).orElseThrow(() -> new ResourceNotFoundException("Refresh token not found"));

            refreshTokenService.verifyExpiration(refreshToken);

            User user = refreshToken.getUser();
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserName());
            String newJwt = jwtUtils.generateToken(userDetails);

            refreshTokenService.deleteByUser(user);
            RefreshToken newRefresh = refreshTokenService.createRefreshToken(user);

            ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", newRefresh.getToken())
                    .httpOnly(true)
                    .secure(false)
                    .sameSite("Lax")
                    .path("/")
                    .maxAge(Duration.between(Instant.now(), newRefresh.getExpiryDate()).getSeconds())
                    .build();

            ResponseCookie newAccessCookie = ResponseCookie.from("token", newJwt)
                    .httpOnly(true)
                    .secure(false)
                    .sameSite("Lax")
                    .path("/")
                    .maxAge(900)
                    .build();

            return ResponseEntity.ok().headers(headers -> {
                        headers.add(HttpHeaders.SET_COOKIE, refreshCookie.toString());
                        headers.add(HttpHeaders.SET_COOKIE, newAccessCookie.toString());
                    })
                    .body(new AuthenticationResponse("Refresh token successfully"));
        } catch (ResourceNotFoundException | RefreshTokenException e) {
            ResponseCookie clearTokenCookie = ResponseCookie.from("token", "")
                    .httpOnly(true)
                    .path("/")
                    .maxAge(0)
                    .build();

            ResponseCookie clearRefreshCookie = ResponseCookie.from("refreshToken", "")
                    .httpOnly(true)
                    .path("/")
                    .maxAge(0)
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .header(HttpHeaders.SET_COOKIE, clearTokenCookie.toString())
                    .header(HttpHeaders.SET_COOKIE, clearRefreshCookie.toString())
                    .body(new AuthenticationResponse("Token expired or invalid"));
        }
    }

    public ResponseEntity<AuthenticationResponse> register(RegisterRequest request) {
        if (userRepository.findByUserName(request.getUserName()).isPresent()) {
            throw new DuplicateResourceException("User already exists");
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
        return login(new AuthenticationRequestDto(request.getUserName(), request.getPassword()));
    }

    public ResponseEntity<AuthenticationResponse> logout(String refreshToken) {
        try {
            if (refreshToken == null || refreshToken.isBlank()) {
                log.warn("Logout attempted without refresh token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new AuthenticationResponse("refresh token missing"));
            }
            refreshTokenService
                    .findByToken(refreshToken)
                    .ifPresent(token -> {
                        refreshTokenService.deleteByUser(token.getUser());
                        log.info("Refresh token deleted for user {}", token.getUser().getUserName());
                    });
            ResponseCookie clearTokenCookie = ResponseCookie.from("token", "")
                    .httpOnly(true)
                    .path("/")
                    .maxAge(0)
                    .build();
            ResponseCookie clearRefreshCookie = ResponseCookie.from("refreshToken", "")
                    .httpOnly(true)
                    .secure(false)
                    .sameSite("Lax")
                    .path("/")
                    .maxAge(0)
                    .build();
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, clearTokenCookie.toString())
                    .header(HttpHeaders.SET_COOKIE, clearRefreshCookie.toString())
                    .body(new AuthenticationResponse("Logged out successfully"));
        } catch (Exception e) {
            log.warn("Error while logging out: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AuthenticationResponse("Error while logging out"));
        }
    }

    public ResponseEntity<MeResponse> me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MeResponse(null, Collections.emptyList(), "Not authenticated"));
        }

        Object principal = authentication.getPrincipal();
        String username;
        List<String> roles;
        if (principal instanceof UserDetails user) {
            username = user.getUsername();
            roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        } else {
            username = principal != null ? principal.toString() : "unknown";
            roles = List.of();
        }

        return ResponseEntity.ok(new MeResponse(username, roles, "Authenticated"));
    }
}