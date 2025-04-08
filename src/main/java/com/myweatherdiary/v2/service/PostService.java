package com.myweatherdiary.v2.service;

import com.myweatherdiary.v2.domain.diary.Diary;
import com.myweatherdiary.v2.domain.post.Post;
import com.myweatherdiary.v2.domain.post.PostDto;
import com.myweatherdiary.v2.repository.DiaryRepository;
import com.myweatherdiary.v2.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.rmi.NoSuchObjectException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    public final DiaryRepository diaryRepository;
    public final PostRepository postRepository;

    public Long post(String username, PostDto postDto) throws NoSuchObjectException {
        try{
            Diary found = diaryRepository.findFirstByUsername(username).get();
            Post post = Post.builder()
                    .diary(found)
                    .pictures(postDto.getPictures())
                    .writing(postDto.getWriting())
                    .postDate(postDto.getPostDate())
                    .updatedDate(postDto.getUpdatedDate())
                    .build();
            return postRepository.save(post).getId();
        } catch (Exception e) {
            throw new NoSuchObjectException("다이어리가 존재하지 않습니다");
        }
    }

    // ToDo-페이징 처리
    public List<PostDto> getPosts(Long diaryId) throws NoSuchObjectException {
        Optional<Diary> byId = diaryRepository.findById(diaryId);
        if (byId.isPresent()) {
            Diary found = byId.get();
            List<PostDto> postDtos = new ArrayList<>();
            postRepository.findAllByDiary(found).forEach(
                    post -> postDtos.add(
                            PostDto.builder()
                                    .id(post.getId())
                                    .pictures(post.getPictures())
                                    .postDate(post.getPostDate())
                                    .updatedDate(post.getUpdatedDate())
                                    .writing(post.getWriting())
                                    .build()
                    )
            );

            return postDtos;
        } else {
            throw new NoSuchObjectException("다이어리가 존재하지 않습니다");
        }
    }


    public PostDto getOnePost(Long postId) throws NoSuchObjectException {
        Optional<Post> byId = postRepository.findById(postId);
        if (byId.isPresent()){
            Post found = byId.get();
            return PostDto.builder()
                    .id(found.getId())
                    .writing(found.getWriting())
                    .pictures(found.getPictures())
                    .postDate(found.getPostDate())
                    .updatedDate(found.getUpdatedDate())
                    .build();
        } else {
            throw new NoSuchObjectException("게시글을 찾을 수 없습니다.");
        }

    }

    public PostDto update(PostDto postDto) throws NoSuchObjectException {
        Optional<Post> byId = postRepository.findById(postDto.getId());
        if (byId.isPresent()){
            Post found = byId.get();
            Post saved = postRepository.save(
                    Post.builder()
                            .id(found.getId())
                            .pictures(postDto.getPictures())
                            .updatedDate(LocalDateTime.now())
                            .postDate(postDto.getPostDate())
                            .writing(postDto.getWriting())
                            .build()
            );
            return PostDto.builder()
                    .id(saved.getId())
                    .pictures(saved.getPictures())
                    .writing(saved.getWriting())
                    .postDate(saved.getPostDate())
                    .updatedDate(saved.getUpdatedDate())
                    .build();
        } else {
            throw new NoSuchObjectException("게시글을 찾을 수 없습니다.");
        }
    }

    public void delete(Long postId) throws NoSuchObjectException {
        Optional<Post> byId = postRepository.findById(postId);
        if (byId.isPresent()){
            postRepository.delete(byId.get());
        } else {
            throw new NoSuchObjectException("게시글이 존재하지 않습니다.");
        }
    }
}
