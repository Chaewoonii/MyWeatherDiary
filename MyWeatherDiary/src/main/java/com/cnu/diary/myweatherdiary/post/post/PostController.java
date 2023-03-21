package com.cnu.diary.myweatherdiary.post.post;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/diary")
public class PostController {

    @Autowired
    PostService postService;

    /*
    //로그인 후 마이페이지. id 값이 있어야 함. >> id 없어도 될듯. js에서 들고 있게?
    @GetMapping("/{pswd_id}")
    public ModelAndView myDiary(@PathVariable("pswd_id") UUID id){
        System.out.println("**");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(new PostEntity(id));
        modelAndView.setViewName("posts");
        return modelAndView;
    }
*/
    // 같은 다이어리(pw가 같은)의 모든 포스트를 불러옴 ->10개씩 불러오기 수정
    @GetMapping({"/timeline/{user_id}"})
    public Iterable<Post> getTimelinePost(@PathVariable("user_id") String id){
        return postService.getAllPostsById(id);
    }

    //일기 저장(db insert)
    @PostMapping("/addPost")
    public Post addPost(@RequestBody PostContentDto postContentDto){
        log.info("{}", postContentDto.toString());
        postService.addPost(postContentDto);
        return null;
    }

    //하나의 포스트만 가져오기. id값 필요.
    @GetMapping("/post/{id}")
    public Post getPost(@PathVariable("id") String id){
        return postService.getPost(id);
    }

    //포스트 수정
    @PutMapping("/post/edit")
    public Post editPost(@RequestBody Post post){
        return postService.modifyPost(post);
    }

    //포스트 삭제
    @DeleteMapping("/remove/{id}")
    public void removePost(@PathVariable("id") String id){
        postService.removePost(id);
    }
}
