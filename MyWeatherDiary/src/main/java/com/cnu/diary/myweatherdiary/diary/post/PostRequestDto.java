package com.cnu.diary.myweatherdiary.diary.post;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostRequestDto {
    private UUID id;

    private Emotion emotion;
    private LocalDateTime postDate;

    public PostRequestDto(Emotion emotion, LocalDateTime postDate) {
        this.emotion = emotion;
        this.postDate = postDate;
    }
}
