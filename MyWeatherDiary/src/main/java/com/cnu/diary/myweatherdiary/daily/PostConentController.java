package com.cnu.diary.myweatherdiary.daily;

import com.cnu.diary.myweatherdiary.daily.content.Content;
import com.cnu.diary.myweatherdiary.daily.content.ContentService;
import com.cnu.diary.myweatherdiary.daily.post.Post;
import com.cnu.diary.myweatherdiary.daily.post.PostDto;
import com.cnu.diary.myweatherdiary.daily.post.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    /*
    //로그인 후 마이페이지. id 값이 있어야 함. >> id 없어도 될듯. js에서 들고 있게?
    @GetMapping("/{pswd_id}")
    public ModelAndView myDiary(@PathVariable("pswd_id") UUID id){
        System.out.println("**");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(new PostEntity(id));
        modelAndView.setViewName("posts");
        return modelAndView;
    }*/

    // 같은 다이어리(pw가 같은)의 모든 포스트를 불러옴 -> 10개씩 불러오기 수정
    @GetMapping({"/timeline/{user_id}"})
    public Iterable<Post> getTimelinePost(@PathVariable("user_id") UUID id){
        return postService.getAllPostsById(id);
    }

    //일기 저장(db insert)
    @PostMapping("/addPost")
    public Post addPost(@RequestBody PostContentDto postContentDto){
        Post post = postService.addPost(new PostDto(
                UUID.randomUUID(),
                postContentDto.getUserId(),
                postContentDto.getEmotion(),
                postContentDto.getPostDate()));

        contentService.saveContents(postContentDto.getContents(), post);
        return post;
    }

    //하나의 포스트만 가져오기. id값 필요.
    @GetMapping("/post/{id}")
    public Post getPost(@PathVariable("id") UUID postId){
        Post post = postService.getPost(postId);
        List<Content> contentList = contentService.findByPostId(post);
        post.setContents(contentList);
        return post;
    }

    /**
     * 유저의 전체 포스트 중에서 writtenDate 전부 가져오기!
     * 포스트 하나당 컨텐츠 최대 10개임
     * 포스트 조회 요청 시 latestDate 를 기준으로 최근 5개씩. <- paging????
     * */

    //포스트 수정
    @PutMapping("/post/edit")
    public Post editPost(@RequestBody PostContentDto postContentDto){

        Post post = postService.updatePost(new PostDto(
                postContentDto.getPostId(),
                postContentDto.getUserId(),
                postContentDto.getEmotion(),
                postContentDto.getPostDate()
        ));

        List<Content> contentList = contentService.updateContents(postContentDto.getContents(), post);
        post.setContents(contentList);
        return post;
    }

    //포스트 삭제
    @DeleteMapping("/remove/{id}")
    public void removePost(@PathVariable("id") UUID id){
        postService.removePost(id);
    }
}
