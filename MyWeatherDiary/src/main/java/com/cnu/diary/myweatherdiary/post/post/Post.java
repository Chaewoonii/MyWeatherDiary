package com.cnu.diary.myweatherdiary.post.post;

import com.cnu.diary.myweatherdiary.post.content.Content;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ToString
@Entity(name = "posts")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @Column(nullable = false, unique = true)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String id;

    @Column(name = "user_id", nullable = false, updatable = false)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String userId;

    @Enumerated(EnumType.ORDINAL)
    private Emotion emotion;

    @Column(name = "post_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime postDate;

    @Column(name = "written_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime writtenDate;


    // post(1) : contents(N).
    @OneToMany(mappedBy = "post")
    private List<Content> contents = new ArrayList<>();


    public void addContent(Content content){content.setPost(this);}

}
