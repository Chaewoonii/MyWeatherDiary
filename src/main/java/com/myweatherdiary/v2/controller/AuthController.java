package com.myweatherdiary.v2.controller;

import com.myweatherdiary.v2.jwt.JwtUtil;
import com.myweatherdiary.v2.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;

    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> request){
        String username = request.get("username");
        String password = request.get("password");

        // 사용자 인증
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        // JWT 토큰 생성
        return jwtUtil.generateToken(username);
    }

    // 로그아웃: 토큰을 블랙리스트에 등록한다 -> `서버의 상태를 변경`하기 때문에 POST
    // REST에서 상태를 변경하는 작업은 POST, PUT, DELETE를 사용한다.
    // GET은 상태 변경을 유발하지 않아야 한다.
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token){
        String extractedToken = token.substring(7);
        redisService.saveBlacklist(extractedToken);
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }

}
