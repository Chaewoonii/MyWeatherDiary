package com.myweatherdiary.v2.domain.diary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DiaryDto {
    private Long id;
    private String diaryTitle;
}
