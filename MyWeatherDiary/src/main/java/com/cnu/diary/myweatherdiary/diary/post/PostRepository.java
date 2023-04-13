package com.cnu.diary.myweatherdiary.diary.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID>{

    @Query("select posts from posts where username = :username")
    Iterable<Post> findAllByUsername(String username);

    Page<Post> findAllByUsernameOrderByPostDateDesc(String username, Pageable pageable);



}
