package com.myweatherdiary.v2.controller;

import com.myweatherdiary.v2.domain.diary.DiaryDto;
import com.myweatherdiary.v2.jwt.JwtUtil;
import com.myweatherdiary.v2.repository.DiaryRepository;
import com.myweatherdiary.v2.service.DiaryService;
import jakarta.persistence.DiscriminatorValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DiaryControllerTest {

    @Autowired
    private DiaryController diaryController;

    @Autowired
    private DiaryRepository diaryRepository;

    @Autowired
    private DiaryService diaryService;

    //ToDo: mockMVC 테스트 코드로 수정
    @Test
    @DisplayName("다이어리 생성")
    public void createDiary(){
        //given
        String diaryTitle = "testDiary";
        DiaryDto request = DiaryDto.builder()
                .diaryTitle(diaryTitle)
                .build();

        //when
        String secret = diaryController.createDiary(request);

        //then: 저장된 이름이 다이어리 생성 시 입력한 이름과 동일해야 한다
        String[] s = secret.split("_");
        String foundTitle = diaryRepository.findFirstByUsername(s[0]).get().getDiaryTitle();
        assertThat(foundTitle).isEqualTo(diaryTitle);
    }

    @Test
    @DisplayName("다이어리 조회")
    public void findDiary(){
        //given
        String[] s = diaryService.register("test").split("_"); //username, password


        //when

        //then
    }

}