package com.myweatherdiary.v2.domain.diary;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiaryDto {
    private Long id;
    private String username;
    private String enterKey;
    private String diaryTitle;
}
