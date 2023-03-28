package com.cnu.diary.myweatherdiary.users.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class UserRegisterDto {
    private String diaryTitle;
    private Optional<String> email = Optional.empty();
    private Long role;
}
