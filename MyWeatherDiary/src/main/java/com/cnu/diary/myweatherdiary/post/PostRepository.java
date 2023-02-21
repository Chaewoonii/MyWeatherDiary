package com.cnu.diary.myweatherdiary.post;

import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, UUID>{

    @Query("select id, user_id, post_comment, post_date, reg_date, mod_date from posts where user_id = :user_id")
    Iterable<PostEntity> findAllByPswd_id(UUID user_id);


}
