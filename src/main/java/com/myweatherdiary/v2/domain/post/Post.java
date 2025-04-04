package com.myweatherdiary.v2.domain.post;

import com.myweatherdiary.v2.domain.diary.Diary;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "post")
public class Post {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id")
    private Diary diary; // 다이어리

    @OneToMany(mappedBy = "post")
    private List<Picture> pictures = new ArrayList<>();

    private String writing;

    private LocalDateTime postDate;

    private LocalDateTime updatedDate;
}
