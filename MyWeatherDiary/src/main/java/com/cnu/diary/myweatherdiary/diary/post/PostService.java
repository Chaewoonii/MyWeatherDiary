package com.cnu.diary.myweatherdiary.diary.post;

import com.cnu.diary.myweatherdiary.exception.PostNotFoundException;
import com.cnu.diary.myweatherdiary.utill.EntityConverter;
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
    private PostRepository postRepository;

    @Autowired
    private EntityConverter entityConverter;


    @Transactional
    @PostMapping("posts")
    public PostResponseDto addPost(String username, PostRequestDto postRequestDto) {

        Post post = Post.builder()
                .username(username)
                .postDate(postRequestDto.getPostDate())
                .emotion(postRequestDto.getEmotion())
                .writtenDate(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS))
                .build();

        return entityConverter.convertPostToDto(postRepository.save(post));
    }

    @Transactional
    @PostMapping("posts")
    public PostResponseDto updatePost(PostRequestDto postRequestDto) {
        Post found = postRepository.findById(postRequestDto.getId()).orElseThrow(
                () -> new PostNotFoundException("Not found post - id: "+ postRequestDto.getId().toString())
        );
        Post post = postRepository.save(
                Post.builder()
                        .id(postRequestDto.getId())
                        .postDate(postRequestDto.getPostDate())
                        .emotion(postRequestDto.getEmotion())
                        .writtenDate(found.getWrittenDate())
                        .username(found.getUsername())
                        .build()
        );

        return entityConverter.convertPostToDto(postRepository.save(post));
    }

    @Transactional
    @GetMapping("posts")
    public PostResponseDto getPost(UUID id) {
        PostResponseDto postResponseDto = entityConverter.convertPostToDto(
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
    public List<PostResponseDto> getAllPostsByUsername(String username) {
        List<PostResponseDto> postResponseDtos = new ArrayList<>();
        postRepository.findAllByUsername(username).forEach(
                p -> postResponseDtos.add(entityConverter.convertPostToDto(p))
        );
        return postResponseDtos;
    }

    @Transactional
    public List<PostResponseDto> getTimelinePost(String username, Pageable pageable) {
        List<PostResponseDto> postResponseDtos = new ArrayList<>();
        postRepository.findAllByUsernameOrderByPostDateDesc(username, pageable).forEach(
                p -> postResponseDtos.add(entityConverter.convertPostToDto(p))
        );
        return postResponseDtos;
    }

    @Transactional
    @GetMapping("posts")
    public void removePost(UUID id){
        postRepository.deleteById(id);
    }

}
