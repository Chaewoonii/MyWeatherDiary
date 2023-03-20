package com.cnu.diary.myweatherdiary.post.post;

import com.cnu.diary.myweatherdiary.post.content.Content;
import com.cnu.diary.myweatherdiary.post.content.ContentRepository;
import com.cnu.diary.myweatherdiary.post.content.Prefix;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class PostService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    ContentRepository contentRepository;

    @Autowired
    EntityManagerFactory emf;


    @Transactional
    @PostMapping("posts")
    public Post addPost(PostContentDto postContentDto) {

        Post post = new Post();
        UUID postId = UUID.randomUUID();
        post.setId(postId);
        post.setPostDate(postContentDto.getPostDate());
        post.setEmotion(postContentDto.getEmotion());
        post.setWrittenDate(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));
        post.setUserId(postContentDto.getUserId());
        postRepository.save(post);

        List<Content> contentList = postContentDto.getContentList(post, Prefix.develop);
        log.info("{}", contentRepository.saveAll(contentList));

        post.setContents(contentList);

        log.info("{}", postRepository.save(post));
        log.info("{}", contentList);
        return null;
    }

    @Transactional
    @PostMapping("posts")
    public Post modifyPost(Post post) {
        return postRepository.save(post);
    }

    @Transactional
    @GetMapping("posts")
    public Post getPost(UUID id) {
        return postRepository.findById(id).get();
    }

    @Transactional
    @GetMapping("posts")
    public Iterable<Post> getAllPostsById(UUID id) {
        return postRepository.findAllByUser_id(id);
    }


    @Transactional
    @GetMapping("posts")
    public void removePost(UUID id){
        postRepository.deleteById(id);
    }
}
