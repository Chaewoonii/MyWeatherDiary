package com.myweatherdiary.v2.service;

import com.myweatherdiary.v2.AuthorizationKeyCreator;
import com.myweatherdiary.v2.domain.diary.Diary;
import com.myweatherdiary.v2.domain.diary.DiaryDto;
import com.myweatherdiary.v2.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final PasswordEncoder passwordEncoder;

    // 다이어리 등록
    // ToDo: diaryTitle 유효성 검사(중복 여부)
    public String register(String diaryTitle){
        String enterKey = createKey();
        Diary diary = Diary.builder()
                .diaryTitle(diaryTitle)
                .username(UUID.randomUUID().toString())
                .enterKey(passwordEncoder.encode(enterKey))
                .build();
        Diary saved = diaryRepository.save(diary);
        return saved.getUsername() + "_" + enterKey;
    }

    public String createKey(){
        return new AuthorizationKeyCreator().getRandomString(10);
    }

    // 다이어리 조회
    public DiaryDto findDiary(String username){
        Optional<Diary> diary = diaryRepository.findFirstByUsername(username);
        if (diary.isPresent()){
            Diary found = diary.get();
            return DiaryDto.builder()
                    .id(found.getId())
                    .diaryTitle(found.getDiaryTitle())
                    .username(found.getUsername())
                    .build();
        }else {
            throw  new IllegalStateException("다이어리 정보 없음");
        }
    }
}
