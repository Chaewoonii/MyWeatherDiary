package com.cnu.diary.myweatherdiary;

import com.cnu.diary.myweatherdiary.daily.PostConentController;
import com.cnu.diary.myweatherdiary.daily.PostContentDto;
import com.cnu.diary.myweatherdiary.daily.content.Content;
import com.cnu.diary.myweatherdiary.daily.content.ContentDto;
import com.cnu.diary.myweatherdiary.daily.post.*;
import com.cnu.diary.myweatherdiary.users.User;
import com.cnu.diary.myweatherdiary.users.UserDto;
import com.cnu.diary.myweatherdiary.users.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PostContentTest {


    @Autowired
    PostConentController postConentController;

    @Autowired
    UserService userService;

    User user;

    Post post;


    @BeforeAll
    void setUp(){
        UserDto userDto = new UserDto();
        userDto.setDiaryTitle("테스트 일기장 >_<");

        this.user = userService.register(userDto);
    }

    @Test
    @Order(1)
    @DisplayName("포스트를 여러 콘텐츠를 포함하여 저장할 수 있다")
    void testAddPost(){
        ContentDto dto1 = new ContentDto(UUID.randomUUID(),"오늘 날씨가 좋다", "img1");
        ContentDto dto2 = new ContentDto(UUID.randomUUID(), "", "img2");
        ContentDto dto3 = new ContentDto(UUID.randomUUID(), "밥이 맛있네", "");
        List<ContentDto> dtoList = List.of(dto1, dto2, dto3);

        PostContentDto postContentDto = new PostContentDto();
        postContentDto.setUserId(user.getId());
        postContentDto.setEmotion(Emotion.HAPPY);
        postContentDto.setPostDate(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));
        postContentDto.setContents(dtoList);

        post = postConentController.addPost(postContentDto);
        log.info("Saved post is:\n{}", post);
    }

    @Test
    @Order(2)
    @DisplayName("userId로 포스트와 콘텐츠 정보를 불러올 수 있다")
    void testSelect(){
        Post post1 = postConentController.getPost(post.getId());
        log.info("found -> {}", post1);
    }

    @Test
    @Order(3)
    @DisplayName("포스트와 콘텐츠 내용을 수정할 수 있다")
    void testUpdate(){
        Post post1 = postConentController.getPost(post.getId());
        Iterator<Content> contents = post1.getContents().iterator();
        List<ContentDto> dtoList = new ArrayList<>();
        List<String> comments = List.of("아이고", "집에", "보내주세요");
        int i = 0;


        while (contents.hasNext()){
            Content content = contents.next();
            dtoList.add(new ContentDto(content.getId(), comments.get(i), "img" + i++));
        }

        PostContentDto postContentDto = new PostContentDto();
        postContentDto.setPostId(post1.getId());
        postContentDto.setUserId(user.getId());
        postContentDto.setEmotion(Emotion.NEUTRAL);
        postContentDto.setPostDate(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));
        postContentDto.setContents(dtoList);

        Post edited = postConentController.editPost(postContentDto);
        log.info("edited -> {}", edited);
    }


    @Test
    @Order(4)
    @DisplayName("포스트가 삭제되면 콘텐츠도 삭제된다")
    void testDelete(){
        postConentController.removePost(post.getId());
    }

}