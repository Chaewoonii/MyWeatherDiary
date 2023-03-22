package com.cnu.diary.myweatherdiary.daily.post;

import com.cnu.diary.myweatherdiary.daily.content.ContentRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Transactional
    @PostMapping("posts")
    public Post addPost(PostDto postDto) {

        Post post = new Post();
        post.setPostDate(postDto.getPostDate());
        post.setEmotion(postDto.getEmotion());
        post.setWrittenDate(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));
        post.setUserId(postDto.getUserId());
        postRepository.save(post);

        return post;
    }

    //포스트, 콘텐츠 수정..
    @Transactional
    @PostMapping("posts")
    public Post updatePost(PostDto postDto) {
        Post post = postRepository.findById(postDto.getId()).orElseThrow();
        post.setPostDate(postDto.getPostDate());
        post.setEmotion(postDto.getEmotion());

        return postRepository.save(post);
    }

    @Transactional
    @GetMapping("posts")
    public Post getPost(UUID id) {
        return postRepository.findById(id).get();
    }

    @Transactional
    @GetMapping("posts")
    public List<Post> getAllPostsById(UUID id) {
        List<Post> posts = new ArrayList<>();
        postRepository.findAllByUser_id(id).forEach(posts::add);
        return posts;
    }


    @Transactional
    @GetMapping("posts")
    public void removePost(UUID id){
        postRepository.deleteById(id);
    }
}
