package com.cnu.diary.myweatherdiary;

import com.cnu.diary.myweatherdiary.users.UserController;
import com.cnu.diary.myweatherdiary.users.UserService;
import com.cnu.diary.myweatherdiary.users.dto.LoginRequestDto;
import com.cnu.diary.myweatherdiary.users.dto.UserRegisterDto;
import com.cnu.diary.myweatherdiary.users.dto.UserResponseDto;
import com.cnu.diary.myweatherdiary.users.repository.UserRepository;
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
    void testFind(){
        log.info("find All -> {}", userService.findAll());
        log.info("find One -> {}", userService.findById(user.getId()));
        log.info("find by enterKey -> {}", userRepository.findByEnterKey(user.getEnterKey()));

    }

    @Test
    void testLogin(){
        LoginRequestDto loginRequestDto = new LoginRequestDto(user.getId().toString(), user.getEnterKey());
        log.info("after login -> {}", userController.login(loginRequestDto));
    }

}
