package com.cnu.diary.myweatherdiary.user;

import com.cnu.diary.myweatherdiary.jwt.JwtAuthentication;
import com.cnu.diary.myweatherdiary.users.UserController;
import com.cnu.diary.myweatherdiary.users.UserService;
import com.cnu.diary.myweatherdiary.users.domain.User;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
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

    String email;
    String key;
    
    @BeforeEach
    void setUp(){
        //Given
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        userRegisterDto.setDiaryTitle("test-diary");
        userRegisterDto.setEmail("testId");
        userRegisterDto.setRole(1L);

        UserRegisterDto userRegisterDto2 = new UserRegisterDto();
        userRegisterDto2.setDiaryTitle("admin-diary");
        userRegisterDto2.setEmail("admin");
        userRegisterDto2.setRole(2L);

        //When
        user = userService.register(userRegisterDto);
        UserResponseDto user2 = userService.register(userRegisterDto2);
        log.info("registered user -> {}", user);
        log.info("registered user2 -> {}", user2);

        email = user.getEmail();
        key = user.getEnterKey();

    }

    @AfterEach
    void tearDown(){
        LoginRequestDto loginRequestDto = new LoginRequestDto(email, key);
        UserTokenDto token = userController.login(loginRequestDto);
        JwtAuthentication authentication = new JwtAuthentication(token.getToken(), token.getEmail());

        userController.removeUser(authentication);
        log.info("after delete -> {}", userService.findAll());
    }


    @Test
    @DisplayName("유저 등록")
    void testRegister() throws Exception{
        //Given
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        userRegisterDto.setDiaryTitle("test-diary");
        userRegisterDto.setEmail("testRegister");
        userRegisterDto.setRole(1L);


        //When
        mockMvc.perform(post("/api/v1/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userRegisterDto)))
                .andExpect(status().isOk())
                .andDo(document("user-register",
                        requestFields(
                                fieldWithPath("diaryTitle").description("DiaryTitle is essentially required"),
                                fieldWithPath("email").description("Email is essential"),
                                fieldWithPath("role").description("User: 1, Admin: 2")
                        ),
                        responseFields(
                                fieldWithPath("id").description("id"),
                                fieldWithPath("email").description("email"),
                                fieldWithPath("enterKey").description("enterKey"),
                                fieldWithPath("diaryTitle").description("diaryTitle"),
                                fieldWithPath("nickName").description("nickName")
                        )));
    }


    @Test
    @DisplayName("로그인")
    void testLogin() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto(email, key);
//        log.info("after login -> {}", userController.login(loginRequestDto));

        mockMvc.perform(post("/api/v1/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andExpect(status().isOk())
                .andDo(document("user-login",
                        requestFields(
                                fieldWithPath("email").description("User ID"),
                                fieldWithPath("enterKey").description("Enter key")
                        ),
                        responseFields(
                               fieldWithPath("token").description("생성된 토큰"),
                               fieldWithPath("username").description("유저 식별자"),
                               fieldWithPath("group").description("유저 그룹")
                        )));

    }

    @Test
    @DisplayName("유저 정보 조회")
    @WithMockUser("testId")
    void testFindUser() throws Exception{
        LoginRequestDto loginRequestDto = new LoginRequestDto(email, key);
        UserTokenDto token = userController.login(loginRequestDto);
        JwtAuthentication authentication = new JwtAuthentication(token.getToken(), token.getEmail());
        UserResponseDto user = userController.getUser(loginRequestDto, authentication);
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
                                fieldWithPath("id").description("id"),
                                fieldWithPath("email").description("email"),
                                fieldWithPath("enterKey").description("enterKey"),
                                fieldWithPath("diaryTitle").description("Diary Title"),
                                fieldWithPath("nickName").description("Nick Name")
                        )));
    }

    @Test
    @DisplayName("유저 정보 수정")
    void testUpdateUser(){
        LoginRequestDto loginRequestDto = new LoginRequestDto(email, key);
        UserTokenDto token = userController.login(loginRequestDto);
        JwtAuthentication authentication = new JwtAuthentication(token.getToken(), token.getEmail());

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
        userRegisterDto.setEmail("testUser");
        userRegisterDto.setRole(1L);

        UserResponseDto user1 = userService.register(userRegisterDto);
        log.info("registered user -> {}", user1);

        LoginRequestDto loginRequestDto = new LoginRequestDto(user1.getEmail(), user1.getEnterKey());
        UserTokenDto token = userController.login(loginRequestDto);
        JwtAuthentication authentication = new JwtAuthentication(token.getToken(), token.getEmail());

        userController.removeUser(authentication);
        log.info("after delete -> {}", userService.findAll());
    }

    @Test
    void testFindByDiaryTitle(){
        UserRegisterDto userRegisterDto1 = new UserRegisterDto();
        userRegisterDto1.setDiaryTitle("흑흑어쩌구저쩌구");
        userRegisterDto1.setRole(1L);

        UserRegisterDto userRegisterDto2 = new UserRegisterDto();
        userRegisterDto2.setDiaryTitle("흑흑어쩌구저쩌구");
        userRegisterDto2.setRole(1L);

        UserResponseDto user1 = userService.register(userRegisterDto1);
        UserResponseDto user2 = userService.register(userRegisterDto2);

        Optional<User> user1optional = userRepository.findByDiaryTitleAndId(user1.getId(), user1.getDiaryTitle());
        Optional<User> user2optional = userRepository.findByDiaryTitleAndId(user2.getId(), user1.getDiaryTitle());
        log.info("findByDiaryTitle -> {}", user1optional);
        log.info("findByDiaryTitle -> {}", user2optional);
    }

}
