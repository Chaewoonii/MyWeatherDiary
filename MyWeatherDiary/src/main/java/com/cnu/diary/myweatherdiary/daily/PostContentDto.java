package com.cnu.diary.myweatherdiary.daily;

import com.cnu.diary.myweatherdiary.daily.content.Content;
import com.cnu.diary.myweatherdiary.daily.content.ContentDto;
import com.cnu.diary.myweatherdiary.daily.content.ContentImgHandler;
import com.cnu.diary.myweatherdiary.daily.content.Prefix;
import com.cnu.diary.myweatherdiary.daily.post.Emotion;
import com.cnu.diary.myweatherdiary.daily.post.Post;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
    private UUID postId; // X
    private UUID userId; //프론트에서 주는게 아니라 session에서 받아오기
    private Emotion emotion;
    private LocalDateTime postDate;
    private List<ContentDto> contents;

    /*public List<Content> getContentList(Post post, Prefix prefix){
        List<Content> contentList = new ArrayList<>();
        Iterator<ContentDto> dtoIterator = this.contents.iterator();

        while (dtoIterator.hasNext()){
            ContentDto dto = dtoIterator.next();

            Content content = Content.builder()
                    .comment(dto.getComment())
                    .prefix(prefix)
                    .imgName(dto.getImg())
                    .build();
            post.addContent(content);
            contentList.add(content);
        }
        return contentList;
    }*/

    public List<byte[]> getImgBytesList(){
        Iterator<ContentDto> iterator = this.contents.iterator();
        ContentImgHandler contentImgHandler = new ContentImgHandler();

        List<byte[]> byteImages = new ArrayList<>();
        while (iterator.hasNext()){
            String base64ImgString = iterator.next().getImg();
            byte[] imgBytes = contentImgHandler.getImgBytes(base64ImgString);
            byteImages.add(imgBytes);
        }
        return byteImages;
    }

}
