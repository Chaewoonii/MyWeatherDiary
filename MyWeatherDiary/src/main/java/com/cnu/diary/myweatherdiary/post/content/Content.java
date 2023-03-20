package com.cnu.diary.myweatherdiary.post.content;

import com.cnu.diary.myweatherdiary.post.post.Post;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;


@Setter
@Getter
@Entity(name = "contents")
@NoArgsConstructor
@AllArgsConstructor
public class Content {

    @Id
    @Column(name = "id")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;
    @Enumerated(EnumType.STRING)
    private Prefix prefix;
    @Column(name = "comment", nullable = true)
    @Lob
    private String comment;
    @Column(name = "img_saved_date", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime imageSavedDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="post_id", referencedColumnName = "id")
    private Post post;


    public void setPost(Post post){
        if (Objects.nonNull(this.post)){
            post.getContents().remove(this);
        }

        this.post = post;
        post.getContents().add(this);
    }
}
