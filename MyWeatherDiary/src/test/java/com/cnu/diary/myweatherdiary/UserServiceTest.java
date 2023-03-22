package com.cnu.diary.myweatherdiary;


import com.cnu.diary.myweatherdiary.users.User;
import com.cnu.diary.myweatherdiary.users.UserDto;
import com.cnu.diary.myweatherdiary.users.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTest {

    @Autowired
    UserService userService;

    User user;

    @Test
    @Order(1)
    @DisplayName("유저를 등록할 수 있다")
    void testRegister(){
        UserDto userDto = new UserDto();
        userDto.setDiaryTitle("테스트 일기장 >_<");

        user = userService.register(userDto);
        User user1 = userService.findById(user.getId());

        log.info("registered -> {}", user);
        log.info("found -> {}", user1);

        assertThat(user, samePropertyValuesAs(user1));
    }

    @Test
    @Order(2)
    @DisplayName("유저 정보를 수정할 수 있다")
    void testUpdate(){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setNickName(user.getNickName());
        userDto.setDiaryTitle("일기일기!!");
        userDto.setNickName("채우닝");
        userDto.setEmail(Optional.of("coco@gmail.com"));

        User updatedUser = userService.updateUserInfo(userDto);

        log.info("updated -> {}", updatedUser);
    }

    @Test
    @Order(3)
    @DisplayName("유저 정보를 삭제할 수 있다")
    void testDelete(){
        userService.removeUser(user.getId());
        assertThat(userService.findAll().isEmpty(), is(true));
    }
}
