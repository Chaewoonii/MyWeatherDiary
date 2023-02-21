package com.cnu.diary.myweatherdiary.post;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PostService {

    @Autowired
    PostRepository postRepository;


    @Transactional
    @PostMapping("posts")
    public PostEntity addPost(PostEntity post) {
        post.setReg_date(Timestamp.valueOf(LocalDateTime.now()));
        post.setMod_date(Timestamp.valueOf(LocalDateTime.now()));
        return postRepository.save(post);
    }

    @Transactional
    @PostMapping("posts")
    public PostEntity modifyPost(PostEntity post) {
        post.setMod_date(Timestamp.valueOf(LocalDateTime.now()));
        return postRepository.save(post);
    }


    @Transactional
    @GetMapping("posts")
    public PostEntity getPost(UUID id) {
        return postRepository.findById(id).get();
    }

    @Transactional
    @GetMapping("posts")
    public Iterable<PostEntity> getAllPostsById(UUID id) {
        return postRepository.findAllByPswd_id(id);
    }


    @Transactional
    @GetMapping("posts")
    public void removePost(UUID id){
        postRepository.deleteById(id);
    }
}
