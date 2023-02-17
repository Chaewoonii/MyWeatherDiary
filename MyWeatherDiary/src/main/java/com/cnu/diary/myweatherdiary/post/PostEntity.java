package com.cnu.diary.myweatherdiary.post;

import com.cnu.diary.myweatherdiary.pswd.PswdEntity;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

@ToString
@Entity(name = "posts")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, unique = true)
    private Long id;
    @Column(nullable = false, unique = true, length = 100)
    private Long pswd_id;
    @Column(nullable = false, length = 100)
    private String diary_title;
    @Column(nullable = false)
    private Date post_date;
    @Column(nullable = false, length =10)
    private String feelings;

    @Column(nullable = false)
    private LocalDateTime post_time;
    @Column(length = 300)
    private String post_comment;
    @Column
    private String loc_pic;


    public PostEntity(Long pswd_id) {
        this.pswd_id = pswd_id;
    }
}
