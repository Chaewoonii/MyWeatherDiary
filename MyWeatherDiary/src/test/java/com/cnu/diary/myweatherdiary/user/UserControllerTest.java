package com.cnu.diary.myweatherdiary.user;

import com.cnu.diary.myweatherdiary.jwt.JwtAuthentication;
import com.cnu.diary.myweatherdiary.users.UserController;
import com.cnu.diary.myweatherdiary.users.UserService;
import com.cnu.diary.myweatherdiary.users.dto.*;
import com.cnu.diary.myweatherdiary.users.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Autowired
    private UserRepository userRepository;

    UserResponseDto user;

    String username;
    String key;
    
    @BeforeEach
    void setUp(){
        //Given
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        userRegisterDto.setDiaryTitle("test-diary");
        userRegisterDto.setRole(1L);

        UserRegisterDto userRegisterDto2 = new UserRegisterDto();
        userRegisterDto2.setDiaryTitle("admin-diary");
        userRegisterDto2.setRole(2L);

        //When
        user = userController.register(userRegisterDto).getData();
        UserResponseDto user2 = userController.register(userRegisterDto2).getData();
        log.info("registered user -> {}", user);
        log.info("registered user2 -> {}", user2);

        username = user.getUsername();
        key = user.getEnterKey();

    }

    @AfterEach
    void tearDown(){
        LoginRequestDto loginRequestDto = new LoginRequestDto(username, key);
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
        mockMvc.perform(post("/api/v1/user")
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
                        )));
    }


    @Test
    @DisplayName("로그인")
    void testLogin() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto(username, key);

        mockMvc.perform(post("/api/v1/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andExpect(status().isOk())
                .andDo(document("user-login",
                        requestFields(
                                fieldWithPath("username").description("User ID"),
                                fieldWithPath("enterKey").description("Enter key")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("Http status code"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("data"),
                                fieldWithPath("data.token").description("token"),
                                fieldWithPath("data.username").description("username"),
                                fieldWithPath("data.group").description("group"),
                                fieldWithPath("serverDateTime").description("serverDateTime")
                        )));

    }

    @Test
    @DisplayName("유저 정보 조회")
    @WithMockUser("testId")
    void testFindUser() throws Exception{
        LoginRequestDto loginRequestDto = new LoginRequestDto(username, key);
        UserTokenDto token = userController.login(loginRequestDto).getData();
        JwtAuthentication authentication = new JwtAuthentication(token.getToken(), token.getUsername());
        UserResponseDto user = userController.getUser(loginRequestDto, authentication).getData();
        log.info("found user -> {}", user);

        mockMvc.perform(get("/api/v1/user/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andExpect(status().isOk())
                .andDo(document("user-get",
                        requestFields(
                                fieldWithPath("email").description("User ID"),
                                fieldWithPath("enterKey").description("Enter key")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("Http status code"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("data"),
                                fieldWithPath("serverDateTime").description("serverDateTime")
                        )));
    }

    @Test
    @DisplayName("유저 정보 수정")
    void testUpdateUser(){
        LoginRequestDto loginRequestDto = new LoginRequestDto(username, key);
        UserTokenDto token = userController.login(loginRequestDto).getData();
        JwtAuthentication authentication = new JwtAuthentication(token.getToken(), token.getUsername());

        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setNickName("충남대 귀요미");
        userRequestDto.setDiaryTitle("충대 일기");
        userRequestDto.setEmail("cnu123");

        userController.updateUserInfo(userRequestDto, authentication);
    }

    @Test
    @DisplayName("유저 정보 삭제")
    void testDeleteUesr(){
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        userRegisterDto.setDiaryTitle("test-diary");
        userRegisterDto.setRole(1L);

        UserResponseDto user1 = userController.register(userRegisterDto).getData();
        log.info("registered user -> {}", user1);

        LoginRequestDto loginRequestDto = new LoginRequestDto(user1.getUsername(), user1.getEnterKey());
        UserTokenDto token = userController.login(loginRequestDto).getData();
        JwtAuthentication authentication = new JwtAuthentication(token.getToken(), token.getUsername());

        userController.removeUser(authentication);
        log.info("after delete -> {}", userService.findAll());
    }


}
