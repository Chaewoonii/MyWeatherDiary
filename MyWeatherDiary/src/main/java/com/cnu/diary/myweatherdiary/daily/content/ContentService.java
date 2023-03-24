package com.cnu.diary.myweatherdiary.daily.content;

import com.cnu.diary.myweatherdiary.daily.post.Post;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Base64.Decoder;
@Service
public class ContentService {

    @Autowired
    ContentRepository contentRepository;

    @Transactional
    @PostMapping("contents")
    public List<Content> saveContents(List<ContentDto> contentDtos, Post post) {
        List<Content> contentList = new ArrayList<>();
        Iterator<ContentDto> iterator = contentDtos.iterator();

        while(iterator.hasNext()){
            ContentDto dto = iterator.next();

            Content content = new Content();
            content.setComment(dto.getComment());
            content.setPrefix(Prefix.develop);
            content.setImageSavedDate(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));
            post.addContent(content);
            contentList.add(content);
        }

        return contentRepository.saveAll(contentList);
    }

    @Transactional
    @PostMapping("contents")
    public List<Content> updateContents(List<ContentDto> contentDtos, Post post){
        List<Content> contentList = new ArrayList<>();
        Iterator<ContentDto> iterator = contentDtos.iterator();

        while (iterator.hasNext()){
            ContentDto dto = iterator.next();

            Content content = contentRepository.findById(dto.getId()).orElseThrow();
            content.setComment(dto.getComment());
            content.setImageSavedDate(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));
            contentList.add(content);
        }
        return contentRepository.saveAll(contentList);
    }

    public List<Content> findByPostId(Post post) {
        return contentRepository.findAllByPost(post);
    }

    @Transactional
    @PostMapping("contents")
    public byte[] imgToBase64(ContentDto contentDto) { // encoder
        Encoder encoder = Base64.getEncoder();
        byte[] encoderByte = encoder.encode(contentDto.getImg().getBytes());

        return encoderByte;
    }


    @Transactional
    public byte[] base64ToImg(byte[] encode) { // decoder
        Decoder decoder = Base64.getDecoder();
        byte[] decoderByte = decoder.decode(encode);

        return  decoderByte;
    }
}
