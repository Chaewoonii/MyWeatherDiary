package com.myweatherdiary.v2.domain.diary;


import com.myweatherdiary.v2.domain.post.Post;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Entity(name = "diary")
@NoArgsConstructor
@AllArgsConstructor
public class Diary {

    @Id @GeneratedValue
    private Long id;

    private String enterKey;

    private String diaryTitle;

    @OneToMany(mappedBy = "diary")
    private List<Post> posts = new ArrayList<>();

}
