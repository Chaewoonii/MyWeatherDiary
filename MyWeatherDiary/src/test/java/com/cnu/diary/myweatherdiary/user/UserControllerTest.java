package com.cnu.diary.myweatherdiary.user;

import com.cnu.diary.myweatherdiary.jwt.JwtAuthentication;
import com.cnu.diary.myweatherdiary.users.UserController;
import com.cnu.diary.myweatherdiary.users.UserDetailService;
import com.cnu.diary.myweatherdiary.users.UserService;
import com.cnu.diary.myweatherdiary.users.dto.*;
import com.cnu.diary.myweatherdiary.users.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.HashMap;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Slf4j
@AutoConfigureRestDocs
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserController userController;

    UserResponseDto user;

    String username;
    String key;

    String token;
    
    @BeforeEach
    void setUp(){
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        userRegisterDto.setDiaryTitle("test-diary");
        userRegisterDto.setRole(1L);
//        user = userController.register(userRegisterDto).getData();
        log.info("registered user -> {}", user);

        /*UserRegisterDto userRegisterDto2 = new UserRegisterDto();
        userRegisterDto2.setDiaryTitle("admin-diary");
        userRegisterDto2.setRole(2L);
        UserResponseDto user2 = userController.register(userRegisterDto2).getData();
        log.info("registered user2 -> {}", user2);*/

        username = user.getUsername();
        key = user.getEnterKey();

        LoginRequestDto loginRequestDto = new LoginRequestDto(key);
        UserTokenDto userTokenDto = userController.login(loginRequestDto).getData();
        token = "Bearer " + userTokenDto.getToken();
    }

    @AfterEach
    void tearDown(){
        LoginRequestDto loginRequestDto = new LoginRequestDto(key);
        UserTokenDto token = userController.login(loginRequestDto).getData();
        JwtAuthentication authentication = new JwtAuthentication(token.getToken(), token.getUsername());

        userController.removeUser(authentication);
        log.info("after delete -> {}", userService.findAll());
    }

    @Test
    @DisplayName("유저 등록")
    void testRegister() throws Exception{
        //Given
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        userRegisterDto.setDiaryTitle("test-diary");
        userRegisterDto.setRole(1L);

        //When
        mockMvc.perform(post("/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userRegisterDto)))
                .andExpect(status().isOk())
                .andDo(document("user-register",
                        requestFields(
                                fieldWithPath("diaryTitle").description("DiaryTitle is essentially required"),
                                fieldWithPath("role").description("User: 1, Admin: 2")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("Http status code"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("data"),
                                fieldWithPath("data.enterKey").description("enterKey"),
                                fieldWithPath("data.diaryTitle").description("diaryTitle"),
                                fieldWithPath("data.nickName").description("nickName"),
                                fieldWithPath("data.username").description("username"),
                                fieldWithPath("serverDateTime").description("serverDateTime")
                        )))
                .andDo(print());
    }

    @Test
    @DisplayName("로그인")
    void testLogin() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto(key);

        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andExpect(status().isOk())
                .andDo(document("user-login",
                        requestFields(
                                fieldWithPath("enterKey").description("Enter key")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("Http status code"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("data"),
                                fieldWithPath("data.token").description("token"),
                                fieldWithPath("data.username").description("username"),
                                fieldWithPath("data.group").description("group"),
                                fieldWithPath("serverDateTime").description("serverDateTime")
                        )))
                .andDo(print());

    }

    @Test
    @DisplayName("유저 정보 조회")
    void testFindUser() throws Exception{
        LoginRequestDto loginRequestDto = new LoginRequestDto(key);
        String afterLoginToken = "Bearer " + userController.login(loginRequestDto).getData().getToken();

        mockMvc.perform(get("/user/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, afterLoginToken))
                .andExpect(status().isOk())
                .andDo(document("user-get",
                        responseFields(
                                fieldWithPath("statusCode").description("Http status code"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("data"),
                                fieldWithPath("data.enterKey").description("enterKey"),
                                fieldWithPath("data.diaryTitle").description("diaryTitle"),
                                fieldWithPath("data.nickName").description("nickName"),
                                fieldWithPath("data.username").description("username"),
                                fieldWithPath("serverDateTime").description("serverDateTime")
                        )))
                .andDo(print());
    }

    @Test
    @DisplayName("유저 정보 수정")
    void testUpdateUser() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto(key);
        String afterLoginToken = "Bearer " + userController.login(loginRequestDto).getData().getToken();

        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setNickName("충남대 귀요미");
        userRequestDto.setDiaryTitle("충대 일기");

        mockMvc.perform(put("/user/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, afterLoginToken)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isOk())
                .andDo(document("user-update",
                        requestFields(
                                fieldWithPath("diaryTitle").description("DiaryTitle is essentially required"),
                                fieldWithPath("nickName").description("Set user nick name")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("Http status code"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("data"),
                                fieldWithPath("data.enterKey").description("enterKey"),
                                fieldWithPath("data.diaryTitle").description("diaryTitle"),
                                fieldWithPath("data.nickName").description("nickName"),
                                fieldWithPath("data.username").description("username"),
                                fieldWithPath("serverDateTime").description("serverDateTime")
                        )))
                .andDo(print());
    }

    @Test
    @DisplayName("유저 정보 삭제")
    void testDeleteUesr() throws Exception {
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        userRegisterDto.setDiaryTitle("test-diary");
        userRegisterDto.setRole(1L);

//        UserResponseDto user1 = userController.register(userRegisterDto).getData();
//        log.info("registered user -> {}", user1);
//
//        LoginRequestDto loginRequestDto = new LoginRequestDto(user1.getEnterKey());
//        UserTokenDto userTokenDto = userController.login(loginRequestDto).getData();
//        String token = "Bearer " + userTokenDto.getToken();

        mockMvc.perform(delete("/user/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk()).andDo(document("user-delete")).andDo(print());

        log.info("after delete -> {}", userService.findAll());
    }

}
