package com.cnu.diary.myweatherdiary.post.post;

import com.cnu.diary.myweatherdiary.post.content.Content;
import com.cnu.diary.myweatherdiary.post.content.ContentDto;
import com.cnu.diary.myweatherdiary.post.content.Prefix;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Slf4j
@ToString
@Getter
@Setter
public class PostContentDto {
    private UUID userId;
    private Emotion emotion;
    private LocalDateTime postDate;
    private List<ContentDto> contents;

    public List<Content> getContentList(Post post, Prefix prefix){
        List<Content> contentList = new ArrayList<>();
        Iterator<ContentDto> dtoIterator = this.contents.iterator();
        int i = 0;
        while (dtoIterator.hasNext()){
            ContentDto contentDto = dtoIterator.next();

            Content content = new Content();
            content.setId(UUID.randomUUID());
            content.setComment(contentDto.getComment());
            content.setPrefix(prefix);
            content.setImageSavedDate(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));
            post.addContent(content);
            contentList.add(content);
            log.info("{}", i++);
        }
        return contentList;
    }

}
