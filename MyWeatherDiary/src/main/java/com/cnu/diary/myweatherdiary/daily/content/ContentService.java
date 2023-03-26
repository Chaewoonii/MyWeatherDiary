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
    public List<byte[]> imgToBase64(List<ContentDto> contentDtos) { // encoder
        List<byte[]> contentImgToByteList = new ArrayList<>();
        Iterator<ContentDto> iter = contentDtos.iterator();

        Encoder encoder = Base64.getEncoder();
        while(iter.hasNext()) {
            ContentDto contentDto = iter.next();
            byte[] contentDtoImgByte = contentDto.getImg().getBytes();
            byte[] encoding = encoder.encode(contentDtoImgByte);

            contentImgToByteList.add(encoding);
        }

        return contentImgToByteList;
    }


    @Transactional
    public List<String> base64ToImg(List<byte[]> contentImgToByteList) { // decoder
        List<String> contentByteToImgList = new ArrayList<>();
        Iterator<byte[]> iter = contentImgToByteList.iterator();

        Decoder decoder = Base64.getDecoder();
        while(iter.hasNext()) {
            byte[] byteImg = iter.next();

            byte[] decoding =  decoder.decode(byteImg);
            contentByteToImgList.add(new String(decoding));
        }

        return contentByteToImgList;
    }
}
