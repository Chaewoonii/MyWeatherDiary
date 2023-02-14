package com.cnu.diary.myweatherdiary.post;

import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long>{

    @Query("select diary_title, feelings, post_comment, post_date, loc_pic from posts where pswd_id = :id")
    Iterable<PostEntity> findAllByPswd_id(Long id);

}
