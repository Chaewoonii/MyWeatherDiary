package com.cnu.diary.myweatherdiary.diary.post;

import com.cnu.diary.myweatherdiary.diary.content.ContentRepository;
import com.cnu.diary.myweatherdiary.exception.PostNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class PostService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    ContentRepository contentRepository;

    public PostResponseDto convertPostToDto(Post post){
        PostResponseDto postResponseDto = new PostResponseDto();
        postResponseDto.setId(post.getId());
        postResponseDto.setEmotion(post.getEmotion());
        postResponseDto.setWrittenDate(post.getWrittenDate());
        return postResponseDto;
    }

    @Transactional
    @PostMapping("posts")
    public PostResponseDto addPost(PostRequestDto postRequestDto) {

        Post post = Post.builder()
                .userId(postRequestDto.getUserId())
                .postDate(postRequestDto.getPostDate())
                .emotion(postRequestDto.getEmotion())
                .writtenDate(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS))
                .build();

        return convertPostToDto(postRepository.save(post));
    }

    @Transactional
    @PostMapping("posts")
    public PostResponseDto updatePost(PostRequestDto postRequestDto) {
        Post post = Post.builder()
                .id(postRequestDto.getId())
                .userId(postRequestDto.getUserId())
                .postDate(postRequestDto.getPostDate())
                .emotion(postRequestDto.getEmotion())
                .build();

        return convertPostToDto(postRepository.save(post));
    }

    @Transactional
    @GetMapping("posts")
    public PostResponseDto getPost(UUID id) {
        PostResponseDto postResponseDto = convertPostToDto(
                postRepository.findById(id).orElseThrow(PostNotFoundException::new)
        );
        return postResponseDto;
    }

    @Transactional
    @GetMapping("posts")
    public Post findPostById(UUID id){
        return postRepository.findById(id).orElseThrow(
                () -> new PostNotFoundException("No such post: " + id)
        );
    }

    @Transactional
    @GetMapping("posts")
    public List<PostResponseDto> getAllPostsById(UUID id) {
        List<PostResponseDto> postResponseDtos = new ArrayList<>();
        postRepository.findAllByUser_id(id).forEach(
                p -> postResponseDtos.add(convertPostToDto(p))
        );
        return postResponseDtos;
    }

    @Transactional
    @GetMapping("posts")
    public void removePost(UUID id){
        postRepository.deleteById(id);
    }

    public List<PostResponseDto> getTimelinePost(UUID id, Pageable pageable) {
        List<PostResponseDto> postResponseDtos = new ArrayList<>();
        postRepository.findAllByUserIdOrderByPostDateDesc(id, pageable).forEach(
                p -> postResponseDtos.add(convertPostToDto(p))
        );
        return postResponseDtos;
    }
}
