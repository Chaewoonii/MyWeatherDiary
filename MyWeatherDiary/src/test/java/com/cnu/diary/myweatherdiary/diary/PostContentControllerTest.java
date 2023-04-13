package com.cnu.diary.myweatherdiary.diary;

import com.cnu.diary.myweatherdiary.diary.content.ContentDto;
import com.cnu.diary.myweatherdiary.diary.content.ContentImgHandler;
import com.cnu.diary.myweatherdiary.diary.post.Emotion;
import com.cnu.diary.myweatherdiary.diary.post.PostResponseDto;
import com.cnu.diary.myweatherdiary.jwt.JwtAuthentication;
import com.cnu.diary.myweatherdiary.users.UserController;
import com.cnu.diary.myweatherdiary.users.dto.LoginRequestDto;
import com.cnu.diary.myweatherdiary.users.dto.UserRegisterDto;
import com.cnu.diary.myweatherdiary.users.dto.UserResponseDto;
import com.cnu.diary.myweatherdiary.users.dto.UserTokenDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Slf4j
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PostContentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    DiaryController diaryController;

    @Autowired
    UserController userController;

    @Autowired
    ContentImgHandler contentImgHandler;

    UserResponseDto user;

    String token;

    PostResponseDto post;

    @BeforeAll
    void setUp() throws IOException {
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        userRegisterDto.setDiaryTitle("테스트 일기장 >_<");
        userRegisterDto.setRole(1L);

        user = userController.register(userRegisterDto).getData();
        LoginRequestDto request = new LoginRequestDto(user.getEnterKey());
        UserTokenDto userTokenDto = userController.login(request).getData();
        token = userTokenDto.getToken();
        JwtAuthentication authentication = new JwtAuthentication(token, userTokenDto.getUsername());

        Optional<String> testImg = contentImgHandler.getBase64ImgFromLocal("testImg");
        Optional<String> testImg2 = contentImgHandler.getBase64ImgFromLocal("testImg2");

        ContentDto dto1 = new ContentDto("오늘 날씨가 좋다", testImg);
        ContentDto dto2 = new ContentDto( "졸려죽음~~", Optional.empty());
        ContentDto dto3 = new ContentDto( "밥이 맛있네", testImg2);
        List<ContentDto> dtoList = List.of(dto1, dto2, dto3);

        PostContentDto postContentDto = new PostContentDto();
        postContentDto.setEmotion(Emotion.HAPPY);
        postContentDto.setPostDate(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));
        postContentDto.setContents(dtoList);

        post = diaryController.addPost(postContentDto, authentication).getData();
        log.info("Saved post is:\n{}", post.getId());
    }

    @Test
    @DisplayName("포스트 저장")
    void testAddPost() throws Exception{
        Optional<String> testImg = contentImgHandler.getBase64ImgFromLocal("testImg");
        Optional<String> testImg2 = contentImgHandler.getBase64ImgFromLocal("testImg2");

        ContentDto dto1 = new ContentDto("어쩌구 저쩌구", testImg);
        ContentDto dto2 = new ContentDto( "허리아픔", Optional.empty());
        ContentDto dto3 = new ContentDto( "밥이 ..", testImg2);
        List<ContentDto> dtoList = List.of(dto1, dto2, dto3);

        PostContentDto postContentDto = new PostContentDto();
        postContentDto.setEmotion(Emotion.HAPPY);
        postContentDto.setPostDate(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));
        postContentDto.setContents(dtoList);

        mockMvc.perform(post("/diary")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(postContentDto))
                    .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andDo(document("post-insert",
                        requestFields(
                                fieldWithPath("postId").type(JsonFieldType.NULL).description("null"),
                                fieldWithPath("emotion").description("emotion"),
                                fieldWithPath("postDate").description("post Date"),
                                fieldWithPath("contents[]").type(JsonFieldType.OBJECT).description("contents"),
                                fieldWithPath("contents[].id").type(JsonFieldType.NULL).description("null or number, reapplied after saving"),
                                fieldWithPath("contents[].comment").description("comment"),
                                fieldWithPath("contents[].img").description("img").ignored()
                        ),
                        responseFields(
                                fieldWithPath("id").description("postId"),
                                fieldWithPath("emotion").description("emotion"),
                                fieldWithPath("postDate").description("post date"),
                                fieldWithPath("writtenDate").description("written date"),
                                fieldWithPath("contents[]").type(JsonFieldType.OBJECT).description("contents"),
                                fieldWithPath("contents[].id").description("null or number, reapplied after saving"),
                                fieldWithPath("contents[].comment").description("comment"),
                                fieldWithPath("contents[].img").description("img").ignored()
                        )))
                .andDo(print());
    }
}