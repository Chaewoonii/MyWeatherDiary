package com.cnu.diary.myweatherdiary.configuration;

import com.cnu.diary.myweatherdiary.jwt.Jwt;
import com.cnu.diary.myweatherdiary.jwt.JwtAuthenticationFilter;
import com.cnu.diary.myweatherdiary.jwt.JwtAuthenticationProvider;
import com.cnu.diary.myweatherdiary.jwt.JwtSecurityContextRepository;
import com.cnu.diary.myweatherdiary.users.UserDetailService;
import com.cnu.diary.myweatherdiary.users.domain.Group;
import com.cnu.diary.myweatherdiary.users.domain.Role;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import java.util.Arrays;


@Slf4j
@Configuration
@EnableWebSecurity
@Setter
public class WebSecurityConfigure{

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
        return (web -> {web.ignoring().requestMatchers("/docs/**", "/login", "/logout");});
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

    public SecurityContextRepository securityContextRepository(){
        Jwt jwt = applicationContext.getBean(Jwt.class);
        return new JwtSecurityContextRepository(jwtConfigure.getHeader(), jwt);
    }

    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:8080",
                "http://localhost:3000",
                "http://ip-10-10-2-10.ap-northeast-2.compute.internal:80",
                "http://10.10.2.10:80",
                "http://myweatherdiary.site"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3000L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .cors().configurationSource(corsConfigurationSource())
                    .and()
                .csrf()
                    .disable()
                .authorizeRequests()
                    .requestMatchers("/diary/**", "/content/**","/auth/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().permitAll()
                        .and()
                /**
                 * formLogin, csrf, headers, http-basic, rememberMe, logout filter 비활성화
                 */
                .formLogin()
                    .disable()
                .headers()
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
                .securityContext()
                    .securityContextRepository(securityContextRepository())
                    .and()
//                .addFilterAfter(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                ;
                return http.build();
    }



}
