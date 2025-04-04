package com.myweatherdiary.v2.controller;

import com.myweatherdiary.v2.domain.post.PostDto;
import com.myweatherdiary.v2.jwt.JwtUtil;
import com.myweatherdiary.v2.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.rmi.NoSuchObjectException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final JwtUtil jwtUtil;

    @PostMapping("/new")
    public Long addPost(@RequestBody PostDto postDto,
                          @AuthenticationPrincipal UserDetails user) throws NoSuchObjectException {
        return postService.post(user.getUsername(), postDto);
    }

    @GetMapping("")
    public PostDto findPost(@RequestParam("id") Long postId) throws NoSuchObjectException {
        return postService.getOnePost(postId);
    }


}
