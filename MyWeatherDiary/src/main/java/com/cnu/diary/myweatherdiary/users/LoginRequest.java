package com.cnu.diary.myweatherdiary.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Getter
public class LoginRequest {
    private String principal;

    private String credentials;
}
