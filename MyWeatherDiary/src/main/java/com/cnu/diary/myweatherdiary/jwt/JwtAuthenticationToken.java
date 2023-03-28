package com.cnu.diary.myweatherdiary.jwt;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;

    //인증 요청시 사용되는 생성자
    public JwtAuthenticationToken(String principal) {
        super(null);
        super.setAuthenticated(false);

        this.principal = principal; //로그인 ID -> user id
    }

    //인증 완료시 사용되는 생성자
    JwtAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        super.setAuthenticated(true);

        this.principal = principal; // 인증에 사용된 객체
    }

    @Override
    public Object getCredentials() {
        return null;
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
    }

}
