package com.cnu.diary.myweatherdiary.post;

import com.cnu.diary.myweatherdiary.users.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PostService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;


    @Transactional
    @PostMapping("posts")
    public PostEntity addPost(PostEntity post) {
        post.setId(UUID.randomUUID());
        Optional<Timestamp> post_Date = Optional.ofNullable(post.getPost_date());
        if (post_Date.isEmpty()) post.setPost_date(Timestamp.valueOf(LocalDateTime.now()));
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
