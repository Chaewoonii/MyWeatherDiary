package com.cnu.diary.myweatherdiary.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.Opt;

import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
public class UserRequestDto {
    private String diaryTitle;
    private Optional<String> email = Optional.empty();
}
