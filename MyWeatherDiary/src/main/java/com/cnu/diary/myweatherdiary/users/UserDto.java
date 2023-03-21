package com.cnu.diary.myweatherdiary.users;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
public class UserDto {

    private String id;

    private String enterKey;

    private String diaryTitle;

    private Optional<String> nickName;

    private Optional<String> email;
}
