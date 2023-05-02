package com.cnu.diary.myweatherdiary.diary;

import com.cnu.diary.myweatherdiary.diary.content.ContentDto;
import com.cnu.diary.myweatherdiary.diary.content.ContentImgHandler;
import com.cnu.diary.myweatherdiary.diary.post.Emotion;
import com.cnu.diary.myweatherdiary.diary.post.PostResponseDto;
import com.cnu.diary.myweatherdiary.exception.ImgNotFoundException;
import com.cnu.diary.myweatherdiary.jwt.JwtAuthentication;
import com.cnu.diary.myweatherdiary.users.UserController;
import com.cnu.diary.myweatherdiary.users.dto.LoginRequestDto;
import com.cnu.diary.myweatherdiary.users.dto.UserRegisterDto;
import com.cnu.diary.myweatherdiary.users.dto.UserResponseDto;
import com.cnu.diary.myweatherdiary.users.dto.UserTokenDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PostContentTest {


    @Autowired
    DiaryController diaryController;

    @Autowired
    ContentImgHandler contentImgHandler;

    @Autowired
    UserController userController;

    UserResponseDto user;

    PostResponseDto post;

    JwtAuthentication authentication;


    @BeforeAll
    void setUp() throws IOException {
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        userRegisterDto.setDiaryTitle("테스트 일기장 >_<");
        userRegisterDto.setRole(1L);

//        user = userController.register(userRegisterDto).getData();
        LoginRequestDto request = new LoginRequestDto(user.getEnterKey());
        UserTokenDto token = userController.login(request).getData();
        authentication = new JwtAuthentication(token.getToken(), token.getUsername());

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
    @Order(1)
    @DisplayName("포스트를 여러 콘텐츠를 포함하여 저장할 수 있다")
    void testAddPost() throws IOException {
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

        PostResponseDto postResponseDto = diaryController.addPost(postContentDto, authentication).getData();
        log.info("Saved post is:\n{}", postResponseDto.getId());
    }

/*    @Test
    @Order(2)
    @DisplayName("userId로 포스트와 콘텐츠 정보를 불러올 수 있다")
    void testSelect() {
        PostResponseDto post1 = diaryController.findPostByPostId(post.getId()).getData();
        log.info("found -> {}", post1);

        Pageable page = PageRequest.of(0, 5);
        Iterable<PostResponseDto> timelinePost = diaryController.getTimelinePost(page, authentication).getData();
        log.info("page -> {}", timelinePost);
    }*/

    @Test
    @Order(3)
    @DisplayName("포스트와 콘텐츠 내용을 수정할 수 있다")
    void testUpdate() throws IOException {
        PostResponseDto post1 = diaryController.findPostByPostId(post.getId()).getData();
        Iterator<ContentDto> contents = post1.getContents().iterator();

        Optional<String> testImg3 = contentImgHandler.getBase64ImgFromLocal("testImg3");
        List<Optional<String>> images = List.of(testImg3, Optional.empty(), Optional.empty());

        List<ContentDto> dtoList = new ArrayList<>();
        List<String> comments = List.of("아이고", "집에", "보내주세요");

        int i = 0;

        while (contents.hasNext()){
            ContentDto dto = contents.next();
            dtoList.add(new ContentDto(dto.getId(), comments.get(i), dto.getContentOrder(),images.get(i++)));
        }

        PostContentDto postContentDto = new PostContentDto();
        postContentDto.setPostId(post1.getId());
        postContentDto.setEmotion(Emotion.NEUTRAL);
        postContentDto.setPostDate(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));
        postContentDto.setContents(dtoList);

        PostResponseDto edited = diaryController.editPost(postContentDto, authentication).getData();
        log.info("edited -> {}", edited.getId());
    }



    @Test
    @Order(4)
    @DisplayName("콘텐츠를 삭제할 수 있다")
    void testDeleteContents() throws ImgNotFoundException{
        diaryController.removeContent(post.getContents().get(0).getId(), authentication);
    }

    @Test
    @Order(5)
    @DisplayName("포스트가 삭제되면 콘텐츠도 삭제된다")
    void testDeletePost(){
        diaryController.removePost(post.getId(), authentication);
    }

}