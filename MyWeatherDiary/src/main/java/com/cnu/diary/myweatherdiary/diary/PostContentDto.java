package com.cnu.diary.myweatherdiary.diary;

import com.cnu.diary.myweatherdiary.diary.content.ContentDto;
import com.cnu.diary.myweatherdiary.diary.content.ContentImgHandler;
import com.cnu.diary.myweatherdiary.diary.post.Emotion;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime postDate;
    private List<ContentDto> contents;

    public List<byte[]> getImgBytesList(){
        Iterator<ContentDto> iterator = this.contents.iterator();
        ContentImgHandler contentImgHandler = new ContentImgHandler();

        List<byte[]> byteImages = new ArrayList<>();
        while (iterator.hasNext()){
            String base64ImgString = iterator.next().getImg().toString();
            byte[] imgBytes = contentImgHandler.getImgBytesFromString(base64ImgString);
            byteImages.add(imgBytes);
        }
        return byteImages;
    }

}
