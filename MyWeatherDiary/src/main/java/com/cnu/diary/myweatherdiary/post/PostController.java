package com.cnu.diary.myweatherdiary.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/diary")
public class PostController {

    @Autowired
    PostService postService;

    @PostMapping("/{id}")
    public ModelAndView myDiary(@PathVariable("id") Long id){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(new PostEntity(id));
        modelAndView.setViewName("posts");
        return modelAndView;
    }

    @GetMapping({"/timeline/{id}"})
    public Iterable<PostEntity> getTimelinePost(@PathVariable("id") Long id){
        return postService.getAllPostsById(id);
    }

    @PostMapping("/addPost")
    public PostEntity addPost(PostEntity post){
        return postService.postSave(post);
    }

    @GetMapping("/post/{id}")
    public PostEntity getPost(@PathVariable("id") Long id){
        return postService.getPost(id);
    }

    @PostMapping("/post/edit/{id}")
    public PostEntity editPost(@PathVariable("id") Long id, @RequestBody PostEntity post){
        return postService.modifyPost(id, post);
    }

    @PostMapping("/delpost/{id}")
    public void removePost(@PathVariable("id") Long id){
        postService.removePost(id);
    }
}
