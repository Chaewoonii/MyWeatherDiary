package com.cnu.diary.myweatherdiary;

import com.cnu.diary.myweatherdiary.post.content.ContentDto;
import com.cnu.diary.myweatherdiary.post.post.Post;
import com.cnu.diary.myweatherdiary.post.post.PostContentDto;
import com.cnu.diary.myweatherdiary.post.post.PostController;
import com.cnu.diary.myweatherdiary.post.post.PostService;
import com.cnu.diary.myweatherdiary.users.User;
import com.cnu.diary.myweatherdiary.users.UserController;
import com.cnu.diary.myweatherdiary.users.UserDto;
import com.cnu.diary.myweatherdiary.users.UserService;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PostServiceTest {

    @Autowired
    PostService postService;

    @Autowired
    UserService userService;

    User user;


    @BeforeAll
    void setUp(){
        UserDto userDto = new UserDto();
        String id = UUID.randomUUID().toString();
        userDto.setId(id);
        userDto.setDiaryTitle("테스트 일기장 >_<");

        log.info("id -> {}", id);
        log.info("userDto.getId().getBytes().toString() -> {}", userDto.getId().getBytes());
        log.info("registered --> {}",this.userService.register(userDto));
        log.info("findAll -> {}", this.userService.findAll());
        log.info("inserted --> {}",this.userService.findById(user.getId()));
    }

    @Test
    void 저장(){

        ContentDto dto1 = new ContentDto("0", "오늘 날씨가 좋다", "img1");
        ContentDto dto2 = new ContentDto("1", "", "img2");
        ContentDto dto3 = new ContentDto("2", "밥이 맛있네", "");
        List<ContentDto> contents = List.of(dto1, dto2, dto3);

        PostContentDto postContentDto = new PostContentDto();
        postContentDto.setUserId(user.getId());
        postContentDto.setPostDate(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));
        postContentDto.setContentDtos(contents);

        Post post = postService.addPost(postContentDto);
        log.info("Saved post is:\n{}", post);

    }

}