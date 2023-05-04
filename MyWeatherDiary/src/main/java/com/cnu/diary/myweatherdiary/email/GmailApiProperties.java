package com.cnu.diary.myweatherdiary.email;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ToString
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "gmail")
public class GmailApiProperties {
    private String name;
    private String token;
    private String credential;
}
