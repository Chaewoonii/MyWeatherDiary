package com.cnu.diary.myweatherdiary.users.dto;

import lombok.*;

import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserResponseDto {
    private UUID id;
    private String enterKey;
    private String diaryTitle;
    private String nickName;
    private Optional<String> email = Optional.empty();
}
