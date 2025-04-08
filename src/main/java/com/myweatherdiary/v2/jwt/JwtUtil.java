package com.myweatherdiary.v2.jwt;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component //component 등록, 컴포넌트 스캔 대상이 됨. 빈으로 등록
public class JwtUtil{

    private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1시간

    private final SecretKey key;

    public JwtUtil() {
        this.key = getSigningKey();
    }

    private SecretKey getSigningKey(){
        return Jwts.SIG.HS256.key().build();
    }

    public String generateToken(String username){
        return Jwts.builder()
                .issuer("MyWeatherDiary")
                .subject(username)
                .signWith(key)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .compact();
    }

    public String extractUsername(String token) {
        Jws<Claims> jws;
        try{
            jws = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
        }catch (JwtException ex){
            throw new JwtException("유효하지 않은 토큰입니다.", ex);
        }
        return jws.getPayload().getSubject();
    }

    public boolean validateToken(String token){
        try{
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        }catch (JwtException | IllegalArgumentException e){
            return false;
        }
    }

    public long getExpirationTime(String token){
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .getTime();
    }

}
