package com.cnu.diary.myweatherdiary.diary.content;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ContentRepository extends JpaRepository<Content, UUID> {

    List<Content> findAllByPostId(UUID postId);

//    @Query("SELECT Content from Content WHERE post = :postId ORDER BY content_order ASC")
    List<Content> findAllByPostIdOrderByContentOrderAsc(UUID postId);

}
