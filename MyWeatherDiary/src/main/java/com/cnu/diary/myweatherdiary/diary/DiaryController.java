package com.cnu.diary.myweatherdiary.diary;

import com.cnu.diary.myweatherdiary.ApiResponse;
import com.cnu.diary.myweatherdiary.diary.content.ContentDto;
import com.cnu.diary.myweatherdiary.diary.content.ContentService;
import com.cnu.diary.myweatherdiary.diary.post.Post;
import com.cnu.diary.myweatherdiary.diary.post.PostRequestDto;
import com.cnu.diary.myweatherdiary.diary.post.PostResponseDto;
import com.cnu.diary.myweatherdiary.diary.post.PostService;
import com.cnu.diary.myweatherdiary.exception.ImgNotFoundException;
import com.cnu.diary.myweatherdiary.exception.PostNotFoundException;
import com.cnu.diary.myweatherdiary.jwt.JwtAuthentication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

//api는 스프링, 나머지는 프론트.
//엔진엑스가 앞단에서 다 묶어..: weather.com/api/...

@Slf4j
@RestController
@RequestMapping("api/v1/diary")
public class DiaryController {

    @Autowired
    PostService postService;

    @Autowired
    ContentService contentService;

    /**
     * 해당 년도의 전체 포스트 불러오기
     * "2023-04-10" : 4
     * 유저의 전체 포스트 중에서 postDate 전부 가져오기! -> 날짜 list
     *  >> 각 날짜별로 contents 가 몇개인지? -> 날짜(key) : 콘텐츠 갯수(value: int) list
     * */

    @PostMapping("")
    public ApiResponse<PostResponseDto> addPost(@RequestBody PostContentDto postContentDto,
                                                @AuthenticationPrincipal JwtAuthentication authentication) throws IOException {
        PostResponseDto postResponseDto = postService.addPost(authentication.username,
                new PostRequestDto(
                postContentDto.getEmotion(),
                postContentDto.getPostDate()));

        Post post = postService.findPostById(postResponseDto.getId());

        List<ContentDto> contentDtos = contentService.saveContents(postContentDto.getContents(), post);
        postResponseDto.setContentDtos(contentDtos);
        return ApiResponse.ok(postResponseDto);
    }

    @GetMapping({"/posts"})
    public ApiResponse<Iterable<PostResponseDto>> getAllPost(@AuthenticationPrincipal JwtAuthentication authentication) throws PostNotFoundException{
        List<PostResponseDto> posts = postService.getAllPostsByUserId(authentication.username);
        return ApiResponse.ok(posts);
    }

    @GetMapping({""})
    public ApiResponse<Iterable<PostResponseDto>> getTimelinePost(Pageable pageable,
                                                                  @AuthenticationPrincipal JwtAuthentication authentication) throws PostNotFoundException{
        List<PostResponseDto> posts = postService.getTimelinePost(authentication.username, pageable);
        return ApiResponse.ok(posts);
    }

    @GetMapping("/{postId}")
    public ApiResponse<PostResponseDto> findPostByPostId(@PathVariable("postId") UUID postId) throws PostNotFoundException{
        PostResponseDto postResponseDto = postService.getPost(postId);
        List<ContentDto> contentList = contentService.findByPostId(postId);
        postResponseDto.setContentDtos(contentList);
        return ApiResponse.ok(postResponseDto);
    }

    @PutMapping("")
    public ApiResponse<PostResponseDto> editPost(@RequestBody PostContentDto postContentDto,
                                                 @AuthenticationPrincipal JwtAuthentication authentication) throws PostNotFoundException, IOException {

        PostResponseDto postResponseDto = postService.updatePost(authentication.username,
                new PostRequestDto(
                postContentDto.getPostId(),
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