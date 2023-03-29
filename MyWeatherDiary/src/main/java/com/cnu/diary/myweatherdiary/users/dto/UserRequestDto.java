package com.cnu.diary.myweatherdiary.users.dto;

import lombok.*;
import org.checkerframework.checker.nullness.Opt;

import java.util.Optional;
import java.util.UUID;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {

    private UUID id; //없애기
    private String diaryTitle;
    private String nickName;
    private Optional<String> email = Optional.empty();
}
