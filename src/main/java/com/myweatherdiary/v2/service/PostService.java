package com.myweatherdiary.v2.service;

import com.myweatherdiary.v2.domain.diary.Diary;
import com.myweatherdiary.v2.domain.post.Post;
import com.myweatherdiary.v2.domain.post.PostDto;
import com.myweatherdiary.v2.repository.DiaryRepository;
import com.myweatherdiary.v2.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.rmi.NoSuchObjectException;
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

    public List<PostDto> getPosts(Long diaryId) throws NoSuchObjectException {
        try {
            Diary found = diaryRepository.findById(diaryId).get();
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
        } catch (Exception e) {
            throw new NoSuchObjectException("다이어리가 존재하지 않습니다");
        }
    }


    public PostDto getOnePost(Long postId) throws NoSuchObjectException {
        try{
            Post post = postRepository.findById(postId).get();
            return PostDto.builder()
                    .id(post.getId())
                    .writing(post.getWriting())
                    .pictures(post.getPictures())
                    .postDate(post.getPostDate())
                    .updatedDate(post.getUpdatedDate())
                    .build();
        } catch (Exception e) {
            throw new NoSuchObjectException("포스트를 찾을 수 없습니다.");
        }

    }
}
