package com.myweatherdiary.v2.integration;

import com.myweatherdiary.v2.controller.DiaryController;
import com.myweatherdiary.v2.domain.diary.DiaryDto;
import com.myweatherdiary.v2.jwt.JwtUtil;
import com.myweatherdiary.v2.service.JwtUserDetailsService;
import com.myweatherdiary.v2.service.RedisService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcExtensionsKt;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisService redisService;

    @Autowired
    private DiaryController diaryController;

    @BeforeEach
    public void createDiary(){
        DiaryDto request = DiaryDto.builder().diaryTitle("test_diary").build();
        diaryController.createDiary(request);
    }

    @AfterEach
    public void deleteDiary(){

    }

    @Test
    void 로그인(){

    }
}
