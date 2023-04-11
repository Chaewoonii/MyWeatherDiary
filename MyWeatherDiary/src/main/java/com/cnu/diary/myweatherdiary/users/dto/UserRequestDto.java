package com.cnu.diary.myweatherdiary.users.dto;

import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {

    private String diaryTitle;
    private String nickName;

}
