package com.cnu.diary.myweatherdiary.jwt;

import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.ToString;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Jwt {
    private final String issuer;

    private final String clientSecret;

    private final int expirySeconds;

    private final Algorithm algorithm;
    private final JWTVerifier jwtVerifier;

    public Jwt(String issuer, String clientSecret, int expirySeconds){
        this.issuer = issuer;
        this.clientSecret = clientSecret;
        this.expirySeconds = expirySeconds;
        this.algorithm = Algorithm.HMAC512(clientSecret);
        this.jwtVerifier = com.auth0.jwt.JWT.require(algorithm)
                .withIssuer(issuer)
                .build();
    }

    //인증요청을 받아 토큰 발급
    public String sign(Claims claims){
        Date now = new Date(); //JWT에서 Date를 받아서 LocalDateTime이 아닌 Date를 받음
        JWTCreator.Builder builder = com.auth0.jwt.JWT.create();
        builder.withIssuer(issuer);
        builder.withIssuedAt(now);
        if (expirySeconds > 0){
            builder.withExpiresAt(new Date(now.getTime() + expirySeconds * 1_000L));
        }
        builder.withClaim("username", claims.username);
        builder.withArrayClaim("roles", claims.roles);
        return builder.sign(algorithm);
    }

    //토큰을 받아 인증
    public Claims verify(String token) throws JWTVerificationException {
        return new Claims(jwtVerifier.verify(token));
    }


    @ToString
    static public class Claims{
        String username;
        String[] roles;
        Date issuedAt;
        Date expiredAt;

        private Claims(){/*no-op*/}

        Claims(DecodedJWT decodedJWT){
            Claim username = decodedJWT.getClaim("username");
            if (!username.isNull()){
                this.username = username.asString();
            }

            Claim roles = decodedJWT.getClaim("roles");
            if (!roles.isNull()){
                this.roles = roles.asArray(String.class);
            }
            this.issuedAt = decodedJWT.getIssuedAt();
            this.expiredAt = decodedJWT.getExpiresAt();
        }

        //factory method
        public static Claims from(String username, String[] roles){
            Claims claims = new Claims();
            claims.username = username;
            claims.roles = roles;
            return claims;
        }

        //map
        public Map<String, Object> asMap(){
            Map<String, Object> map = new HashMap<>();
            map.put("username", username);
            map.put("roles", roles);
            map.put("issuedAt", toTimestamp(issuedAt));
            map.put("expiredAt", toTimestamp(expiredAt));

            return map;
        }

        long toTimestamp(Date date){
            return date != null ? date.getTime() : -1;
        }

    }
}
