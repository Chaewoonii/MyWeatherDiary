package com.cnu.diary.myweatherdiary.diary;

import com.cnu.diary.myweatherdiary.ApiResponse;
import com.cnu.diary.myweatherdiary.diary.content.ContentDto;
import com.cnu.diary.myweatherdiary.diary.content.ContentLocalService;
import com.cnu.diary.myweatherdiary.diary.content.ContentS3Service;
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
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

//api는 스프링, 나머지는 프론트.
//엔진엑스가 앞단에서 다 묶어..: weather.com/api/...

@Slf4j
@RestController
@RequestMapping("/diary")
public class DiaryController {

    @Autowired
    private PostService postService;

    @Autowired
    private ContentS3Service contentService;

    @PostMapping("")
    public ApiResponse<PostResponseDto> addPost(@RequestBody PostContentDto postContentDto,
                                                @AuthenticationPrincipal JwtAuthentication authentication) throws IOException {
        PostResponseDto postResponseDto = postService.addPost(authentication.username,
                new PostRequestDto(
                postContentDto.getEmotion(),
                postContentDto.getPostDate()));

        Post post = postService.findPostById(postResponseDto.getId());

        List<ContentDto> contentDtos = contentService.saveContents(postContentDto.getContents(), post);
        postResponseDto.setContents(contentDtos);
        return ApiResponse.ok(postResponseDto);
    }

    @GetMapping("")
    public ApiResponse<List<PostResponseDto>> getTimelinePost(@PageableDefault(size = 5, sort = "postDate", direction = Sort.Direction.DESC) Pageable pageable,
                                                              @AuthenticationPrincipal JwtAuthentication authentication) throws PostNotFoundException{
        List<PostResponseDto> posts = postService.getTimelinePost(authentication.username, pageable);
        posts.forEach(
                p -> p.setContents(contentService.findByPostId(p.getId()))
        );
//        log.info("timeline posts -> {}", posts);
        return ApiResponse.ok(posts);
    }

    @GetMapping("/{postId}")
    public ApiResponse<PostResponseDto> findPostByPostId(@PathVariable("postId") UUID postId) throws PostNotFoundException{
        PostResponseDto postResponseDto = postService.getPost(postId);
        List<ContentDto> contentList = contentService.findByPostId(postId);
        postResponseDto.setContents(contentList);
        return ApiResponse.ok(postResponseDto);
    }

    @PostMapping("/activity")
    public ApiResponse<List<PostResponseDto>> getPostsByIdList(@RequestBody List<UUID> idList){
        List<PostResponseDto> posts = postService.getPostsByIdList(idList);
        posts.forEach(
                p -> p.setContents(contentService.findByPostId(p.getId()))
        );
        return ApiResponse.ok(posts);
    }

    @GetMapping("/activity/{year}")
    public List<PostResponseDto> getPostsByYear(@PathVariable("year") int year,
                                                             @AuthenticationPrincipal JwtAuthentication authentication){
        return postService.getPostsByYear(authentication.username, year);
    }

    @PutMapping("")
    public ApiResponse<PostResponseDto> editPost(@RequestBody PostContentDto postContentDto,
                                                 @AuthenticationPrincipal JwtAuthentication authentication) throws PostNotFoundException, IOException {
        PostResponseDto postResponseDto = postService.updatePost(
                new PostRequestDto(
                postContentDto.getPostId(),
                postContentDto.getEmotion(),
                postContentDto.getPostDate()
        ));

        Post post = postService.findPostById(postResponseDto.getId());

        List<ContentDto> contentList = contentService.updateContents(postContentDto.getContents(), post);
        postResponseDto.setContents(contentList);
        return ApiResponse.ok(postResponseDto);
    }

    @DeleteMapping("/{postId}")
    public ApiResponse<String> removePost(@PathVariable("postId") UUID postId,
                                          @AuthenticationPrincipal JwtAuthentication authentication) throws PostNotFoundException{
        contentService.deleteAllContentsByPostId(postId);
        postService.removePost(postId);
        return ApiResponse.ok("Delete Success");
    }

    @GetMapping("/content/{contentId}")
    public ApiResponse<String> getImgSource(@PathVariable("contentId") UUID id){
        return ApiResponse.ok(contentService.getImgBase64FromId(id));
    }

    @GetMapping("/content/all/{contentId}")
    public ApiResponse<ContentDto> getContent(@PathVariable("contentId") UUID id){
        ContentDto contentDto = contentService.findById(id);
        return ApiResponse.ok(contentDto);
    }

    @DeleteMapping("/content/{contentId}")
    public ApiResponse<String> removeContent(@PathVariable("contentId") UUID contentsId,
                                             @AuthenticationPrincipal JwtAuthentication authentication) throws ImgNotFoundException {
//        log.info(authentication.username);
        contentService.deleteContents(contentsId);
        return ApiResponse.ok("Delete Success: user <" + authentication.username + ">");
    }
}
