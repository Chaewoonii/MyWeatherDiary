package com.cnu.diary.myweatherdiary.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

@RestController
@RequestMapping("/diary")
public class PostController {

    @Autowired
    PostService postService;

    @GetMapping("")
    public ModelAndView firstPage(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(new PostEntity());
        modelAndView.setViewName("posts");
        return modelAndView;
    }

    @PostMapping("/newpost")
    public PostEntity newPost(PostEntity post){
        return postService.postSave(post);
    }

    @GetMapping("/posts")
    public Iterable<PostEntity> getMyPosts(){
        return postService.getAllPosts();
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
