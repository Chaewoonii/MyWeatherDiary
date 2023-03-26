package com.cnu.diary.myweatherdiary.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Optional;
import java.util.UUID;

@ToString
@Getter
@Setter
@AllArgsConstructor
public class LoginDto {

    private final String token;
    private final String username;
    private final String group;


}
