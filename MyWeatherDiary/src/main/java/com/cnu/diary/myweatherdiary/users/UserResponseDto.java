package com.cnu.diary.myweatherdiary.users;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@ToString
public class UserResponseDto {
    private UUID id;
    private String enterKey;
    private String diaryTitle;
    private String nickName;
    private Optional<String> email = Optional.empty();
}
