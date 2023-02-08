package com.cnu.diary.myweatherdiary.post;

import jakarta.persistence.*;
import lombok.Getter;

import java.sql.Date;
import java.sql.Time;


@Entity(name = "posts")
@Getter

public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(nullable = false, unique = true, length = 100)
    private int pswd_id;
    private String diary_title;
    private Date post_date;
    private String feelings;
    private Time post_time;
    private String post_comment;
    private String loc_pic;


}
