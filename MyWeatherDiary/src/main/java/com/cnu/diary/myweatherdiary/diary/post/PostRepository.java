package com.cnu.diary.myweatherdiary.diary.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID>{

    @Query("select posts from posts where userName = :username")
    Iterable<Post> findAllByUsername(String username);

    Page<Post> findAllByUserNameOrderByPostDateDesc(String username, Pageable pageable);

    @Query("select id, postDate, emotion from posts where userName = :username and YEAR(postDate) = :year order by postDate asc")
    Iterable<Post> findAllByUserNameAndYear(String username, int year);



}
