package com.springboot.blog.services;

import com.springboot.blog.payloads.LoginDto;
import com.springboot.blog.payloads.RegisterDto;

public interface AuthService {
    String login(LoginDto loginDto);

    String register(RegisterDto registerDto);
}
