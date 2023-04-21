package com.cnu.diary.myweatherdiary.diary.post;

import com.cnu.diary.myweatherdiary.diary.content.Content;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

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
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", nullable = false, unique = true, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "user_name", nullable = false, length = 20)
    private String userName;

    @Enumerated(EnumType.ORDINAL)
    private Emotion emotion;

    @Column(name = "post_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime postDate;

    @Column(name = "written_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime writtenDate;


    // post(1) : contents(N).
    @OneToMany
    @JoinTable(
            name = "post_contents",
            joinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "content_id", referencedColumnName = "id")
    )
    private List<Content> contents = new ArrayList<>();


    public void addContent(Content content){content.setPost(this);}

}
