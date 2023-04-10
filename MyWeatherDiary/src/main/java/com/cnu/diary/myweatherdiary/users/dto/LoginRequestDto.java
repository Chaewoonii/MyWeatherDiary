package com.cnu.diary.myweatherdiary.users.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Getter
@Setter
public class LoginRequestDto {

    private String username;

    private String enterKey;
}
