/*
package com.cnu.diary.myweatherdiary.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.RequestPredicate;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;


import static org.springframework.web.servlet.function.RequestPredicates.accept;
import static org.springframework.web.servlet.function.RouterFunctions.route;

@Configuration(proxyBeanMethods = false)
public class MyRoutingConfiguration {
    private static final RequestPredicate ACCEPT_JSON = accept(MediaType.APPLICATION_JSON);

    @Bean
    public RouterFunction<ServerResponse> routerFunction(MyPswdHandler pswdHandler){
        return route()
                .POST("/{pswd}", ACCEPT_JSON, pswdHandler::savePswd)
                .GET("/{id}", ACCEPT_JSON, pswdHandler::getPswd)
                .build();
    }

}
*/