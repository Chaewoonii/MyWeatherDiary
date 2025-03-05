package com.myweatherdiary.v2.service;

import com.myweatherdiary.v2.AuthorizationKeyCreator;
import com.myweatherdiary.v2.domain.diary.Diary;
import com.myweatherdiary.v2.domain.diary.DiaryDto;
import com.myweatherdiary.v2.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;

    // 다이어리 등록
    // ToDo: diaryTitle 유효성 검사(중복 여부)
    public Long register(String diaryTitle){
        Diary diary = Diary.builder()
                .diaryTitle(diaryTitle)
                .enterKey(createKey())
                .build();
        Diary saved = diaryRepository.save(diary);
        return saved.getId();
    }

    public String createKey(){
        return new AuthorizationKeyCreator().getRandomString(10);
    }

    // 다이어리 조회
    public DiaryDto findDiary(Long diaryId){
        Optional<Diary> diary = diaryRepository.findById(diaryId);
        if (diary.isPresent()){
            Diary found = diary.get();
            return new DiaryDto(found.getId(), found.getDiaryTitle());
        }else {
            throw  new IllegalStateException("다이어리 정보 없음");
        }
    }
}
