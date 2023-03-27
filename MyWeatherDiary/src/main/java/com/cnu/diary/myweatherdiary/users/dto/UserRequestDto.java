package com.cnu.diary.myweatherdiary.users.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.Opt;

import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class UserRequestDto {

    private UUID id;
    private String diaryTitle;
    private Optional<String> email = Optional.empty();
}
