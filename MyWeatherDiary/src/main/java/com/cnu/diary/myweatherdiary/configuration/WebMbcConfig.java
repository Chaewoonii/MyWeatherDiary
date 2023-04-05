package com.cnu.diary.myweatherdiary.configuration;

import com.cnu.diary.myweatherdiary.daily.content.ContentImgHandler;
import com.cnu.diary.myweatherdiary.utill.EntityConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMbcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("POST", "GET", "PUT", "DELETE");
    }

    @Bean
    public EntityConverter convertEntityToDto(){
        return new EntityConverter();
    }

    @Bean
    public ContentImgHandler contentImgHandlerBean(){return new ContentImgHandler(); }

}
