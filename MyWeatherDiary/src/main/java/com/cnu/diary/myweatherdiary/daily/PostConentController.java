package com.cnu.diary.myweatherdiary.daily;

import com.cnu.diary.myweatherdiary.daily.content.Content;
import com.cnu.diary.myweatherdiary.daily.content.ContentDto;
import com.cnu.diary.myweatherdiary.daily.content.ContentService;
import com.cnu.diary.myweatherdiary.daily.post.Post;
import com.cnu.diary.myweatherdiary.daily.post.PostRequestDto;
import com.cnu.diary.myweatherdiary.daily.post.PostResponseDto;
import com.cnu.diary.myweatherdiary.daily.post.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

//api는 스프링, 나머지는 프론트.
//엔진엑스가 앞단에서 다 묶어..: weather.com/api/...

@Slf4j
@RestController
@RequestMapping("api/v1/diary")
public class PostConentController {

    @Autowired
    PostService postService;

    @Autowired
    ContentService contentService;


    /**
     * 유저의 전체 포스트 중에서 postDate 전부 가져오기! -> 날짜 list
     *  >> 각 날짜별로 contents 가 몇개인지? -> 날짜(key) : 콘텐츠 갯수(value: int) list
     * 포스트 하나당 컨텐츠 최대 10개임
     * 포스트 조회 요청 시 latestDate 를 기준으로 최근 5개씩. <- paging????
     * */


    @GetMapping({"/{user_id}"})
    public Iterable<PostResponseDto> getTimelinePost(@PathVariable("user_id") UUID id){
        List<PostResponseDto> posts = postService.getAllPostsById(id);
        return posts;
    }

    @PostMapping("")
    public PostResponseDto savePost(@RequestBody PostContentDto postContentDto) throws IOException{
        PostResponseDto postResponseDto = postService.addPost(new PostRequestDto(
                UUID.randomUUID(),
                postContentDto.getUserId(),
                postContentDto.getEmotion(),
                postContentDto.getPostDate()));


        List<ContentDto> contentDtos = contentService.saveContents(postContentDto.getContents());
        postResponseDto.setContentDtos(contentDtos);
        return postResponseDto;
    }

    @PostMapping("/save/local")
    public PostResponseDto savePostLocal(@RequestBody PostContentDto postContentDto) throws IOException {
        PostResponseDto postResponseDto = postService.addPost(new PostRequestDto(
                UUID.randomUUID(),
                postContentDto.getUserId(),
                postContentDto.getEmotion(),
                postContentDto.getPostDate()));

        List<ContentDto> contentDtos = contentService.saveContents(postContentDto.getContents());
        postResponseDto.setContentDtos(contentDtos);
        return postResponseDto;
    }

    @GetMapping("/{id}")
    public PostResponseDto getPost(@PathVariable("id") UUID postId) throws IOException{
        PostResponseDto postResponseDto = postService.getPost(postId);
        List<ContentDto> contentList = contentService.findByPostId(postId);
        postResponseDto.setContentDtos(contentList);
        return postResponseDto;
    }


    @PutMapping("")
    public PostResponseDto editPost(@RequestBody PostContentDto postContentDto) throws IOException {

        PostResponseDto postResponseDto = postService.updatePost(new PostRequestDto(
                postContentDto.getPostId(),
                postContentDto.getUserId(),
                postContentDto.getEmotion(),
                postContentDto.getPostDate()
        ));

        List<ContentDto> contentList = contentService.updateContents(postContentDto.getContents());
        postResponseDto.setContentDtos(contentList);
        return postResponseDto;
    }

    @DeleteMapping("{id}")
    public void removePost(@PathVariable("id") UUID id){
        postService.removePost(id);
    }
}
