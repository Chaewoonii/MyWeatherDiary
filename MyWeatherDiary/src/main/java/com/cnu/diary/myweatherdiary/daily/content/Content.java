package com.cnu.diary.myweatherdiary.daily.content;

import com.cnu.diary.myweatherdiary.daily.post.Post;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;


@Setter
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
    private Prefix prefix; //develop/2023-03-22askdjgoq.jpg
    @Column(name = "comment", nullable = true)
    @Lob
    private String comment;
    @Column(name = "img_saved_date", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime imageSavedDate;

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
