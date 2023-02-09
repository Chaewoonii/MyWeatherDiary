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
    @Column(nullable = false, unique = true)
    private int id;
    @Column(nullable = false, unique = true, length = 100)
    private int pswd_id;
    @Column(nullable = false, length = 100)
    private String diary_title;
    @Column(nullable = false)
    private Date post_date;
    @Column(nullable = false, length =10)
    private String feelings;
    @Column(nullable = false)
    private Time post_time;
    @Column(length = 300)
    private String post_comment;
    @Column
    private String loc_pic;


}
