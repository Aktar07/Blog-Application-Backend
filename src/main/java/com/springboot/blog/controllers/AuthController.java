package com.springboot.blog.controllers;

import com.springboot.blog.payloads.JwtAuthResponse;
import com.springboot.blog.payloads.LoginDto;
import com.springboot.blog.payloads.RegisterDto;
import com.springboot.blog.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    //build login rest api
    @PostMapping(value = {"/login","/signin"})
    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginDto loginDto){
        String token = authService.login(loginDto);

        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setAccessToken(token);
        return ResponseEntity.ok(jwtAuthResponse);
    }

    //Buid reister rest api
    @PostMapping(value = {"/signup","/register"})
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto){
        String response = authService.register(registerDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
