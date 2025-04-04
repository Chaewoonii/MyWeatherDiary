package com.myweatherdiary.v2.jwt;

import jakarta.annotation.Nullable;

import java.rmi.NoSuchObjectException;

// 인증완료 후 인증된 사용자를 표현
public class JwtAuthentication {
    public final String token;
    public final String username;

    public JwtAuthentication(String token, String username) throws NoSuchObjectException {
        checkArgument(token.isEmpty(), "token must be provided");
        checkArgument(token.isEmpty(), "username must be provided");

        this.token = token;
        this.username = username;
    }

    public void checkArgument(boolean expression, @Nullable Object errorMsg){
        if (!expression){
            throw new IllegalArgumentException(String.valueOf(errorMsg));
        }
    }
}
