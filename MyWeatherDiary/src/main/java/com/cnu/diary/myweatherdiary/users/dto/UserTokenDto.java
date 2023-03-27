package com.cnu.diary.myweatherdiary.users.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@AllArgsConstructor
public class UserTokenDto {

    private final String token;
    private final String username;
    private final String group;


}
