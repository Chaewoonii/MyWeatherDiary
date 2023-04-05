package com.cnu.diary.myweatherdiary.daily.post;

import com.cnu.diary.myweatherdiary.daily.content.ContentDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@ToString
@NoArgsConstructor
@Getter
@Setter
public class PostResponseDto {
    private UUID id;
    private Emotion emotion;
    private LocalDateTime postDate;
    private LocalDateTime writtenDate;
    private List<ContentDto> contentDtos;
}
