package com.cnu.diary.myweatherdiary.configuration;

import com.cnu.diary.myweatherdiary.jwt.Jwt;
import com.cnu.diary.myweatherdiary.jwt.JwtAuthenticationFilter;
import com.cnu.diary.myweatherdiary.jwt.JwtAuthenticationProvider;
import com.cnu.diary.myweatherdiary.users.UserDetailService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.context.SecurityContextHolderFilter;

import javax.servlet.http.HttpServletResponse;


@Slf4j
@Configuration
@EnableWebSecurity
@Setter
public class WebSecurityConfigure {

    private final ApplicationContext applicationContext;
    private final JwtConfigure jwtConfigure;

    public WebSecurityConfigure(ApplicationContext applicationContext, JwtConfigure jwtConfigure){
        this.applicationContext = applicationContext;
        this.jwtConfigure = jwtConfigure;
    }

    @Bean
    public JwtConfigure jwtConfigureBean(){
        return new JwtConfigure();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web -> {web.ignoring().requestMatchers("/templates/**");});
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, e) -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = authentication != null ? authentication.getPrincipal() : null;
            log.warn("{} is denied", principal, e);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("text/plain;charset=UTF-8");
            response.getWriter().write("ACCESS DENIED");
            response.getWriter().flush();
            response.getWriter().close();
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Jwt jwt() {
        return new Jwt(
                jwtConfigure.getIssuer(),
                jwtConfigure.getClientSecret(),
                jwtConfigure.getExpirySeconds()
        );
    }

    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider(Jwt jwt, UserDetailService userDetailService) {
        return new JwtAuthenticationProvider(jwt, userDetailService);
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        Jwt jwt = applicationContext.getBean(Jwt.class);
        return new JwtAuthenticationFilter(jwtConfigure.getHeader(), jwt);
    }


    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .requestMatchers("/api/v1/diary", "/api/v1/auth").hasAnyRole("USER")
                .anyRequest().permitAll()
                .and()
                /**
                 * formLogin, csrf, headers, http-basic, rememberMe, logout filter 비활성화
                 */
                .formLogin()
                .disable()
                .csrf()
                .disable()
                .headers()
                .disable()
                .httpBasic()
                .disable()
                .rememberMe()
                .disable()
                .logout()
                .disable()
                /**
                 * Session 사용하지 않음
                 */
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                /**
                 * 예외처리 핸들러
                 */
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler())
                .and()
                .addFilterAfter(jwtAuthenticationFilter(), SecurityContextHolderFilter.class)
                ;
                return http.build();
    }



}
