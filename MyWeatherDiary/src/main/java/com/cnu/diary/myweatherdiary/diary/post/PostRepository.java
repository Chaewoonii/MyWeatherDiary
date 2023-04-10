package com.cnu.diary.myweatherdiary.diary.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID>{

    @Query("select id, emotion, postDate, writtenDate, contents from posts where userId = :userId")
    Iterable<Post> findAllByUserId(UUID userId);

    Page<Post> findAllByUserIdOrderByPostDateDesc(UUID userId, Pageable pageable);



}
