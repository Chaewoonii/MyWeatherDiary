package com.myweatherdiary.v2.repository;

import com.myweatherdiary.v2.domain.diary.Diary;
import com.myweatherdiary.v2.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByDiary(Diary diary);
}
