package com.myweatherdiary.v2.controller;

import com.myweatherdiary.v2.domain.post.PostDto;
import com.myweatherdiary.v2.jwt.JwtUtil;
import com.myweatherdiary.v2.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.rmi.NoSuchObjectException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final JwtUtil jwtUtil;

    @PostMapping("/new")
    public Long addPost(@RequestBody PostDto postDto,
                        @AuthenticationPrincipal UserDetails token) throws NoSuchObjectException {
        return postService.post(token.getUsername(), postDto);
    }

    @GetMapping("")
    public PostDto findPost(@RequestParam("id") Long postId) throws NoSuchObjectException {
        return postService.getOnePost(postId);
    }

    @PutMapping("")
    public PostDto updatePost(@RequestBody PostDto postDto) throws NoSuchObjectException {
        return postService.update(postDto);
    }

    @DeleteMapping("")
    public ResponseEntity<?> deletePost(@RequestParam("id") Long postId) throws NoSuchObjectException{
        postService.delete(postId);
        return ResponseEntity.ok("게시글이 삭제되었습니다.");
    }

}
