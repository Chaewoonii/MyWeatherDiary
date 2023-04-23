package com.cnu.diary.myweatherdiary.configuration;

import com.cnu.diary.myweatherdiary.diary.content.ContentImgHandler;
import com.cnu.diary.myweatherdiary.utill.EntityConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://192.168.0.46:3000", "http://localhost:3000")
                .allowedMethods("POST", "GET", "PUT", "DELETE", "OPTIONS")
                //.allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(1);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/docs/**").addResourceLocations("classpath:/docs/");
    }

    @Bean
    public EntityConverter convertEntityToDto(){
        return new EntityConverter();
    }

    @Bean
    public ContentImgHandler contentImgHandlerBean(){return new ContentImgHandler(); }

}
