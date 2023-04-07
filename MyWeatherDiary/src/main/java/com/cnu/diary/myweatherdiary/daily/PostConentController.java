package com.cnu.diary.myweatherdiary.daily;

import com.cnu.diary.myweatherdiary.ApiResponse;
import com.cnu.diary.myweatherdiary.daily.content.ContentDto;
import com.cnu.diary.myweatherdiary.daily.content.ContentService;
import com.cnu.diary.myweatherdiary.daily.post.Post;
import com.cnu.diary.myweatherdiary.daily.post.PostRequestDto;
import com.cnu.diary.myweatherdiary.daily.post.PostResponseDto;
import com.cnu.diary.myweatherdiary.daily.post.PostService;
import com.cnu.diary.myweatherdiary.exception.ContentNotFoundException;
import com.cnu.diary.myweatherdiary.exception.ImgNotFoundException;
import com.cnu.diary.myweatherdiary.exception.PostNotFoundException;
import jakarta.persistence.ElementCollection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

//api는 스프링, 나머지는 프론트.
//엔진엑스가 앞단에서 다 묶어..: weather.com/api/...

@Slf4j
@RestController
@RequestMapping("api/v1/post")
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


    @GetMapping({"/{userId}"})
    public ApiResponse<Iterable<PostResponseDto>> getAllPost(@PathVariable("userId") UUID id) throws PostNotFoundException{
        List<PostResponseDto> posts = postService.getAllPostsById(id);
        return ApiResponse.ok(posts);
    }

    @GetMapping({"/timeline/{userId}"})
    public ApiResponse<Iterable<PostResponseDto>> getTimelinePost(@PathVariable("userId") UUID id, Pageable pageable) throws PostNotFoundException{
        List<PostResponseDto> posts = postService.getTimelinePost(id, pageable);
        return ApiResponse.ok(posts);
    }

    @PostMapping("")
    public ApiResponse<PostResponseDto> savePost(@RequestBody PostContentDto postContentDto) throws IOException {
        PostResponseDto postResponseDto = postService.addPost(new PostRequestDto(
                postContentDto.getUserId(),
                postContentDto.getEmotion(),
                postContentDto.getPostDate()));

        Post post = postService.findPostById(postResponseDto.getId());

        List<ContentDto> contentDtos = contentService.saveContents(postContentDto.getContents(), post);
        postResponseDto.setContentDtos(contentDtos);
        return ApiResponse.ok(postResponseDto);
    }


    @GetMapping("/{postId}")
    public ApiResponse<PostResponseDto> findPostByPostId(@PathVariable("postId") UUID postId) throws PostNotFoundException{
        PostResponseDto postResponseDto = postService.getPost(postId);
        List<ContentDto> contentList = contentService.findByPostId(postId);
        postResponseDto.setContentDtos(contentList);
        return ApiResponse.ok(postResponseDto);
    }


    @PutMapping("")
    public ApiResponse<PostResponseDto> editPost(@RequestBody PostContentDto postContentDto) throws PostNotFoundException, IOException {

        PostResponseDto postResponseDto = postService.updatePost(new PostRequestDto(
                postContentDto.getPostId(),
                postContentDto.getUserId(),
                postContentDto.getEmotion(),
                postContentDto.getPostDate()
        ));

        Post post = postService.findPostById(postResponseDto.getId());

        List<ContentDto> contentList = contentService.updateContents(postContentDto.getContents(), post);
        postResponseDto.setContentDtos(contentList);
        return ApiResponse.ok(postResponseDto);
    }

    @DeleteMapping("{postId}")
    public ApiResponse<String> removePost(@PathVariable("postId") UUID postId) throws PostNotFoundException{
        postService.removePost(postId);
        contentService.deleteAllContentsByPostId(postId);
        return ApiResponse.ok("Delete Success");
    }

    @DeleteMapping("/content/{contentsId}")
    public ApiResponse<String> removeContent(@PathVariable("contentsId") UUID contentsId) throws ImgNotFoundException {
        contentService.deleteContents(contentsId);
        return ApiResponse.ok("Delete Success");
    }
}
