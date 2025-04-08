package com.myweatherdiary.v2.jwt;

import com.myweatherdiary.v2.service.RedisService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final RedisService redisService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // 토큰이 Bearer 로 시작하지 않는다면 예외 발생
        if (authHeader == null){
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7); //Bearer제거
        String username = jwtUtil.extractUsername(token); //username 추출

        // 사용자 이름이 null이 아니고, Spring Security context에 인증 정보가 없을 경우, 블랙리스트에 등록된 토큰이 아닐 경우
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null
                && !redisService.isBlacklisted(token)){
            UserDetails userDetails = userDetailsService.loadUserByUsername(username); //사용자 정보 로드
            if (jwtUtil.validateToken(token)){ //토큰 검증
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken); // 인증 정보 설정
            }
        }
        filterChain.doFilter(request, response); // 필터 체인 진행
    }
}
