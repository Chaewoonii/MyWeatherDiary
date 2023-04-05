package com.cnu.diary.myweatherdiary.daily.content;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ContentDto {
    private UUID id;
    private String comment;
    private String img; // base64
}
