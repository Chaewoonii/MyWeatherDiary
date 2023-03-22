package com.cnu.diary.myweatherdiary.daily;

import com.cnu.diary.myweatherdiary.daily.content.Content;
import com.cnu.diary.myweatherdiary.daily.content.ContentDto;
import com.cnu.diary.myweatherdiary.daily.content.Prefix;
import com.cnu.diary.myweatherdiary.daily.post.Emotion;
import com.cnu.diary.myweatherdiary.daily.post.Post;
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

@ToString
@Getter
@Setter
public class PostContentDto {
    private UUID postId;
    private UUID userId;
    private Emotion emotion;
    private LocalDateTime postDate;
    private List<ContentDto> contentDtos;

    public List<Content> getContentList(Post post, Prefix prefix){
        List<Content> contentList = new ArrayList<>();
        Iterator<ContentDto> dtoIterator = this.contentDtos.iterator();

        while (dtoIterator.hasNext()){
            ContentDto contentDto = dtoIterator.next();

            Content content = new Content();
            content.setComment(contentDto.getComment());
            content.setPrefix(prefix);
            content.setImageSavedDate(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));
            post.addContent(content);
            contentList.add(content);
        }
        return contentList;
    }

}
