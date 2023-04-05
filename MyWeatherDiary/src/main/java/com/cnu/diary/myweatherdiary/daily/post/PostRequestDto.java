package com.cnu.diary.myweatherdiary.daily.post;

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
    private UUID userId;
    private Emotion emotion;
    private LocalDateTime postDate;

}
