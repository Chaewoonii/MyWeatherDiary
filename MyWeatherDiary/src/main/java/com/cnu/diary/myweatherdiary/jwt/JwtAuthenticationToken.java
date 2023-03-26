package com.cnu.diary.myweatherdiary.jwt;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;
    private String credentials;

    //인증 요청시 사용되는 생성자
    public JwtAuthenticationToken(String principal, String credentials) {
        super(null);
        super.setAuthenticated(false);

        this.principal = principal; //로그인 ID -> user id, uuid
        this.credentials = credentials; // 로그인 비밀번호 -> enterKey
    }

    //인증 완료시 사용되는 생성자
    JwtAuthenticationToken(Object principal, String credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        super.setAuthenticated(true);

        this.principal = principal; // 인증에 사용된 객체
        this.credentials = credentials; // 비밀번호 딱히 필요 없음 -> null 이 입력될수도
    }

    //토큰이 false이면 IllegalArgumentException이 발생
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        credentials = null;
    }

}
