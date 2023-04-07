package com.cnu.diary.myweatherdiary.daily.content;

import lombok.*;

import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ContentDto {
    private UUID id;
    private String comment;
    private Optional<String> img; // base64

    public ContentDto(String comment, Optional<String> img){
        this.comment = comment;
        this.img = img;
    }
}
