package com.cnu.diary.myweatherdiary.diary.content;

import com.cnu.diary.myweatherdiary.diary.post.Post;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.Objects;
import java.util.UUID;


@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contents")
public class Content {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", unique = true, nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;
    @Enumerated(EnumType.STRING)
    private Prefix prefix; //develop/uuid~~~~.jpg
    @Column(name = "comment", nullable = true)
    @Lob
    private String comment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;


    public void setPost(Post post){
        if (Objects.nonNull(this.post)){
            post.getContents().remove(this);
        }

        this.post = post;
        post.getContents().add(this);
    }

}
