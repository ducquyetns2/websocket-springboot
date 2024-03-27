package com.ducquyet.websocket.controller;

import com.ducquyet.websocket.requestDto.AuthenticationRequest;
import com.ducquyet.websocket.requestDto.UserRegister;
import com.ducquyet.websocket.exception.JwtExpiration;
import com.ducquyet.websocket.service.AuthService;
import com.sun.security.auth.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import responseDto.AuthenticationResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class AuthController {
    private final AuthService authService;
    @GetMapping("/exception")
    ResponseEntity<Object> exception() {
        throw new JwtExpiration("This is the exception");
    }
    @PostMapping("/register")
    void createUser(@RequestBody UserRegister user) {
        authService.register(user);
    }
    @PostMapping("/login")
    ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        return new ResponseEntity<>(authService.login(request), HttpStatus.OK);
    }
    @GetMapping("/logout")
    ResponseEntity<String> logout(Authentication authentication) {
        authService.logout(authentication.getName());
        SecurityContextHolder.getContext().setAuthentication(null);
        return ResponseEntity.ok().body("Log out success");
    }
}