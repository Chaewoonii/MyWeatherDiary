package com.cnu.diary.myweatherdiary.jwt;

import lombok.ToString;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
@ToString
public class JwtAuthentication { //인증완료 후 인증된 사용자를 표현하기 위한 클래스
    //인증 완료 후 JwtAuthenticationToken의 principal에 입력되기 위한 용도

    public final String token;
    public final String username; //enterKey

    JwtAuthentication(String token, String username) {
        checkArgument(isNotEmpty(token), "token must be provided.");
        checkArgument(isNotEmpty(username), "username must be provided.");

        this.token = token;
        this.username = username;
    }
}
