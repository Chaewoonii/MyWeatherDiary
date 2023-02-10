package com.cnu.diary.myweatherdiary.post;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Service
public class PostService {

    @Autowired
    PostRepository postRepository;


    @Transactional
    @PostMapping("posts")
    public PostEntity postSave(PostEntity post) {
        return postRepository.save(post);
    }

    @Transactional
    @GetMapping("posts")
    public PostEntity getPost(Long id) {
        return postRepository.findById(id).get();
    }

    @Transactional
    @GetMapping("posts")
    public Iterable<PostEntity> getAllPosts() {
        return postRepository.findAll();
    }

    @Transactional
    @PostMapping("posts")
    public PostEntity modifyPost(Long id, PostEntity post) {
        PostEntity oldPost = postRepository.getReferenceById(id);
        oldPost.setFeelings(post.getFeelings());
        oldPost.setPost_date(post.getPost_date());
        oldPost.setPost_time(post.getPost_time());
        oldPost.setPost_comment(post.getPost_comment());
        oldPost.setLoc_pic(post.getLoc_pic());
        return postRepository.save(oldPost);
    }

    @Transactional
    @GetMapping("posts")
    public void removePost(Long id){
        postRepository.deleteById(id);
    }
}
