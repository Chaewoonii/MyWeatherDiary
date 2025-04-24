package com.myweatherdiary.v2.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myweatherdiary.v2.domain.diary.DiaryDto;
import com.myweatherdiary.v2.jwt.JwtUtil;
import com.myweatherdiary.v2.repository.DiaryRepository;
import com.myweatherdiary.v2.service.DiaryService;
import jakarta.persistence.DiscriminatorValue;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest
class DiaryControllerTest {

    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DiaryController diaryController;

    private String basicPW;

    @BeforeEach
    @DisplayName("mockMvc 셋팅")
    void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(diaryController).build();

        DiaryDto requestDto = DiaryDto.builder().diaryTitle("basic").build();
        MvcResult mvcResult = mockMvc.perform(post("/diary/register", requestDto)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andReturn();

        this.basicPW = mvcResult.getResponse().getContentAsString();
    }


    //ToDo: mockMVC 테스트 코드로 수정
    @Test
    @DisplayName("다이어리 생성")
    public void createDiary() throws Exception {
        //given
        DiaryDto requestDto = DiaryDto.builder().diaryTitle("test register").build();

        //when
        mockMvc.perform(post("/diary/register", requestDto)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("다이어리 조회")
    public void findDiary(){
       //given

    }

}