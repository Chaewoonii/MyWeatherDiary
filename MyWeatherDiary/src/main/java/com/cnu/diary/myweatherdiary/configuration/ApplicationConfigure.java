package com.cnu.diary.myweatherdiary.configuration;

import com.cnu.diary.myweatherdiary.diary.content.ContentImgHandler;
import com.cnu.diary.myweatherdiary.email.GmailSmtp;
import com.cnu.diary.myweatherdiary.email.GmailSmtpProperties;
import com.cnu.diary.myweatherdiary.utill.EntityConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class ApplicationConfigure implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:3000",
                        "http://myweatherdiary.site"
                )
                .allowedMethods("POST", "GET", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true)
                .maxAge(3000);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/docs/**").addResourceLocations("classpath:/docs/");
    }

    @Bean
    public EntityConverter entityConverter(){
        return new EntityConverter();
    }

    @Bean
    public ContentImgHandler contentImgHandler(){return new ContentImgHandler(); }

    @Bean
    public GmailSmtp gmailSmtp(GmailSmtpProperties properties){return new GmailSmtp(properties); }
}
