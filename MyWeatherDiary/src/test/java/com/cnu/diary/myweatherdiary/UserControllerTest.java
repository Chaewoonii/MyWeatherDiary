package com.cnu.diary.myweatherdiary;

import com.cnu.diary.myweatherdiary.users.UserController;
import com.cnu.diary.myweatherdiary.users.UserService;
import com.cnu.diary.myweatherdiary.users.domain.User;
import com.cnu.diary.myweatherdiary.users.dto.*;
import com.cnu.diary.myweatherdiary.users.repository.UserRepository;
import com.cnu.diary.myweatherdiary.users.utill.AuthorizationKeyCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
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
        user = userService.register(userRegisterDto);
        UserResponseDto user2 = userService.register(userRegisterDto2);
        log.info("registered user -> {}", user);
        log.info("registerd user2 -> {}", user2);

        key = user.getEnterKey();
    }

    @AfterEach
    void tearDown(){
        userService.removeUser(user.getId());
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
                                fieldWithPath("email").description("Email can be nullable"),
                                fieldWithPath("role").description("User: 1, Admin: 2")
                        ),
                        responseFields(
                                fieldWithPath("id").description("id"),
                                fieldWithPath("enterKey").description("enterKey"),
                                fieldWithPath("diaryTitle").description("diaryTitle"),
                                fieldWithPath("nickName").description("nickName"),
                                fieldWithPath("email").description("email")
                        )));
    }


    @Test
    @DisplayName("로그인")
    void testLogin(){
        LoginRequestDto loginRequestDto = new LoginRequestDto(key);
        log.info("after login -> {}", userController.login(loginRequestDto));
    }

    @Test
    @DisplayName("유저 정보 조회")
    void testFindUser(){
        LoginRequestDto loginRequestDto = new LoginRequestDto(key);
        UserTokenDto token = userController.login(loginRequestDto);


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
