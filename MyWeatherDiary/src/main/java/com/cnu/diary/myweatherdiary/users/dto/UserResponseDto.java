package com.cnu.diary.myweatherdiary.users.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserResponseDto {
    private UUID id; //x
    private String enterKey;
    private String diaryTitle;
    private String nickName;
    private String email;
}
