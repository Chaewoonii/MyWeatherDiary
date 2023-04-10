package com.cnu.diary.myweatherdiary.diary;

import com.cnu.diary.myweatherdiary.diary.content.ContentDto;
import com.cnu.diary.myweatherdiary.diary.content.ContentImgHandler;
import com.cnu.diary.myweatherdiary.diary.post.Emotion;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@ToString
@Getter
@Setter
public class PostContentDto {
    private UUID postId;
    private Emotion emotion;
    private LocalDateTime postDate;
    private List<ContentDto> contents;

    public List<byte[]> getImgBytesList(){
        Iterator<ContentDto> iterator = this.contents.iterator();
        ContentImgHandler contentImgHandler = new ContentImgHandler();

        List<byte[]> byteImages = new ArrayList<>();
        while (iterator.hasNext()){
            String base64ImgString = iterator.next().getImg().toString();
            byte[] imgBytes = contentImgHandler.getImgBytes(base64ImgString);
            byteImages.add(imgBytes);
        }
        return byteImages;
    }

}
