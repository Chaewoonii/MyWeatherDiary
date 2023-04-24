package com.cnu.diary.myweatherdiary.diary.post;

import com.cnu.diary.myweatherdiary.diary.content.ContentDto;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime postDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime writtenDate;
    private List<ContentDto> contents;


}
