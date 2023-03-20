package com.cnu.diary.myweatherdiary.post.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID>{

    @Query("select id, emotion, postDate, writtenDate, contents from posts where userId = :userId")
    Iterable<Post> findAllByUser_id(UUID userId);


}
