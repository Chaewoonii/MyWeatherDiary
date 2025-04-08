package com.myweatherdiary.v2.service;

import com.myweatherdiary.v2.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtUtil jwtUtil;

    // 로그아웃 -> 토큰의 만료시간까지 블랙리스트 캐싱
    public void saveBlacklist(String token){
        redisTemplate.opsForValue().set(token, // key
                jwtUtil.extractUsername(token), // value: username
                jwtUtil.getExpirationTime(token), // timeout: expiration time
                TimeUnit.MILLISECONDS); // unit
    }

    public boolean isBlacklisted(String token){
        return Boolean.TRUE.equals(redisTemplate.hasKey(token));
    }
}
