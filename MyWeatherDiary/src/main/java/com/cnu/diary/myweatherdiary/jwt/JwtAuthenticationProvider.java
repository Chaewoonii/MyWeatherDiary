package com.cnu.diary.myweatherdiary.jwt;

import com.cnu.diary.myweatherdiary.users.UserDetailService;
import com.cnu.diary.myweatherdiary.users.domain.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

import static org.apache.commons.lang3.ClassUtils.isAssignable;

@AllArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private Jwt jwt;

    @Autowired
    private UserDetailService userDetailService;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken jwtAuthentication = (JwtAuthenticationToken) authentication;
        return processUserAuthentication(
                String.valueOf(jwtAuthentication.getPrincipal()),
                jwtAuthentication.getCredentials()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return isAssignable(JwtAuthenticationToken.class, authentication);
    }

    private Authentication processUserAuthentication(String principal, String credentials) {
        try {
            User user = userDetailService.login(principal, credentials);
            List<GrantedAuthority> authorities = user.getUserGroup().getAuthorities();
            String token = getToken(user.getId().toString(), authorities);
            JwtAuthenticationToken authenticated =
                    new JwtAuthenticationToken(new JwtAuthentication(token, user.getEnterKey()), null, authorities);
            authenticated.setDetails(user);
            return authenticated;
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException(e.getMessage());
        } catch (DataAccessException e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
    }

    private String getToken(String username, List<GrantedAuthority> authorities) {
        String[] roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .toArray(String[]::new);
        return jwt.sign(Jwt.Claims.from(username, roles));
    }
}
