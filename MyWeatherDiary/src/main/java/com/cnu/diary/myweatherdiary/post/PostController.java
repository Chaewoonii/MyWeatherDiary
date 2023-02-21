package com.cnu.diary.myweatherdiary.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@RestController
@RequestMapping("/diary")
public class PostController {

    @Autowired
    PostService postService;


    // 같은 다이어리(pw가 같은)의 모든 포스트를 불러옴
    @GetMapping({"/timeline/{user_id}"})
    public Iterable<PostEntity> getTimelinePost(@PathVariable("user_id") UUID id){
        return postService.getAllPostsById(id);
    }

    //포스트 생성(db insert)
    @PostMapping("/addPost")
    public PostEntity addPost(@RequestBody PostEntity post){
        return postService.addPost(post);
    }

    //하나의 포스트만 가져오기. id값 필요.
    @GetMapping("/post/{id}")
    public PostEntity getPost(@PathVariable("id") UUID id){
        return postService.getPost(id);
    }

    //포스트 수정
    @PutMapping("/post/edit")
    public PostEntity editPost(@RequestBody PostEntity post){
        System.out.println(post.toString());
        return postService.modifyPost(post);
    }

    //포스트 삭제
    @DeleteMapping("/remove/{id}")
    public void removePost(@PathVariable("id") UUID id){
        postService.removePost(id);
    }
}
