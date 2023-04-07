package com.cnu.diary.myweatherdiary.daily.post;

import com.cnu.diary.myweatherdiary.users.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID>{

    @Query("select id, emotion, postDate, writtenDate, contents from posts where userId = :userId")
    Iterable<Post> findAllByUser_id(UUID userId);

    Page<Post> findAllByUserIdOrderByPostDateDesc(UUID userId, Pageable pageable);



}
