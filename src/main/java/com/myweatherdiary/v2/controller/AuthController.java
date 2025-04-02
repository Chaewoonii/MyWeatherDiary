package com.myweatherdiary.v2.controller;

import com.myweatherdiary.v2.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> request){
        String username = request.get("username");
        String password = request.get("password");

        // 사용자 인증
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        // JWT 토큰 생성
        return jwtUtil.generateToken(username);
    }
}
