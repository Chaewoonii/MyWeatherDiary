package com.cnu.diary.myweatherdiary.users.dto;

import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
public class UserTokenDto {
    private final String token;
    private final String username;
    private final String group;
}
