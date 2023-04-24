package com.cnu.diary.myweatherdiary.diary.post;

import com.cnu.diary.myweatherdiary.exception.PostNotFoundException;
import com.cnu.diary.myweatherdiary.utill.EntityConverter;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
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
    public PostResponseDto addPost(String username, PostRequestDto postRequestDto) {

        Post post = Post.builder()
                .userName(username)
                .postDate(postRequestDto.getPostDate())
                .emotion(postRequestDto.getEmotion())
                .writtenDate(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS))
                .build();

        Post saved = postRepository.save(post);
        log.info("saved -> {}", saved);
        return entityConverter.convertPostToDto(saved);
    }

    @Transactional
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
                        .userName(found.getUserName())
                        .build()
        );

        return entityConverter.convertPostToDto(postRepository.save(post));
    }

    @Transactional
    public PostResponseDto getPost(UUID id) {
        PostResponseDto postResponseDto = entityConverter.convertPostToDto(
                postRepository.findById(id).orElseThrow(PostNotFoundException::new)
        );
        return postResponseDto;
    }

    @Transactional
    public Post findPostById(UUID id){
        return postRepository.findById(id).orElseThrow(
                () -> new PostNotFoundException("No such post: " + id)
        );
    }

    //forEach문 중복코드 함수로 빼기
    @Transactional
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
        postRepository.findAllByUserNameOrderByPostDateDesc(username, pageable).forEach(
                p -> postResponseDtos.add(entityConverter.convertPostToDto(p))
        );
        return postResponseDtos;
    }

    @Transactional
    public void removePost(UUID id){
        postRepository.deleteById(id);
    }

    @Transactional
    public List<PostResponseDto> getPostsByYear(String username, int year) {
        List<PostResponseDto> postResponseDtos = new ArrayList<>();
        postRepository.findByUserNameAndPostDateBetweenOrderByPostDateAsc(
                        username,
                        LocalDateTime.of(year, Month.JANUARY, 1, 0, 0, 0, 0),
                        LocalDateTime.of(year, Month.DECEMBER, 31, 23, 59, 59, 999)
                )
                .forEach(
                        p -> {
                            PostResponseDto dto = new PostResponseDto();
                            BeanUtils.copyProperties(p, dto, "writtenDate", "contents");
                            postResponseDtos.add(dto);
                        }
                );
        return postResponseDtos;
    }

    public List<PostResponseDto> getPostsByIdList(List<UUID> idList) {
        List<PostResponseDto> postResponseDtos = new ArrayList<>();
        idList.forEach(
                id -> postResponseDtos.add(
                        entityConverter.convertPostToDto(
                            postRepository.findById(id).orElseThrow(PostNotFoundException::new)
                    )
                )
        );
        return postResponseDtos;
    }
}
