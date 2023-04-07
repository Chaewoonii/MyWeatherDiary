package com.cnu.diary.myweatherdiary.daily.post;

import com.cnu.diary.myweatherdiary.daily.content.Content;
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

    @Column(name = "user_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID userId;

    @Enumerated(EnumType.ORDINAL)
    private Emotion emotion;

    @Column(name = "post_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime postDate;

    @Column(name = "written_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime writtenDate;


    // post(1) : contents(N).
    @OneToMany(mappedBy = "post", cascade = {CascadeType.ALL})
    private List<Content> contents = new ArrayList<>();


    public void addContent(Content content){content.setPost(this);}

}
