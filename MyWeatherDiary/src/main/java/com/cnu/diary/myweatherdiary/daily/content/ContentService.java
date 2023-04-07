package com.cnu.diary.myweatherdiary.daily.content;

import com.cnu.diary.myweatherdiary.daily.post.Post;
import com.cnu.diary.myweatherdiary.exception.ImgNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Service
public class ContentService {

    @Autowired
    ContentRepository contentRepository;

    @Autowired
    ContentImgHandler contentImgHandler;

    public ContentDto convertContentToDto(Content content){
        ContentDto contentDto = new ContentDto();
        contentDto.setId(content.getId());
        contentDto.setComment(content.getComment());
        contentDto.setImg(
                contentImgHandler.getBase64ImgFromLocal(content.getId().toString())
        );
        return contentDto;
    }
    @Transactional
    @PostMapping("contents")
    public List<ContentDto> saveContents(List<ContentDto> contentDtos, Post post) throws IOException {
        List<ContentDto> contentRequestDtoList = new ArrayList<>();
        Iterator<ContentDto> iterator = contentDtos.iterator();

        while(iterator.hasNext()){
            ContentDto dto = iterator.next();
            Content content = Content.builder()
                    .comment(dto.getComment())
                    .prefix(Prefix.develop)
                    .post(post)
                    .build();

            Content saved = contentRepository.save(content);
            if (dto.getImg().isPresent()){
                contentImgHandler.saveContentsImageLocal(
                        dto.getImg().orElseThrow(() ->new ImgNotFoundException("이미지 없음")),
                        saved.getId().toString());
            }

            contentRequestDtoList.add(convertContentToDto(saved));
        }

        return contentRequestDtoList;
    }



    @Transactional
    @PostMapping("contents")
    public List<ContentDto> updateContents(List<ContentDto> contentDtos, Post post) throws IOException{
        List<ContentDto> contentList = new ArrayList<>();
        Iterator<ContentDto> iterator = contentDtos.iterator();

        while (iterator.hasNext()){
            ContentDto dto = iterator.next();
            Content content = Content.builder()
                    .id(dto.getId())
                    .comment(dto.getComment())
                    .post(post)
                    .build();

            Content saved = contentRepository.save(content);
            if (dto.getImg().isPresent()){
                String savedImg = contentImgHandler.saveContentsImageLocal(
                        dto.getImg().orElseThrow(ImgNotFoundException::new),
                        saved.getId().toString());
                log.info("save success: {}", savedImg);
            }else{
                String deletedImg = contentImgHandler.deleteImg(saved.getId().toString());
                log.info("deleted Image: {}", deletedImg);
            }
            contentList.add(convertContentToDto(saved));
        }
        return contentList;
    }

    public List<ContentDto> findByPostId(UUID id){
        List<ContentDto> contentDtos = new ArrayList<>();
        Iterator<Content> iterator = contentRepository.findAllByPostId(id).iterator();

        while (iterator.hasNext()){
            ContentDto contentDto = convertContentToDto(iterator.next());
            contentDtos.add(contentDto);
        }
        return contentDtos;
    }

    public void deleteContents(UUID id) throws ImgNotFoundException{
        contentRepository.deleteById(id);
        contentImgHandler.deleteImg(id.toString());
    }


    public void deleteAllContentsByPostId(UUID postId) {
        Iterator<Content> contents = contentRepository.findAllByPostId(postId).iterator();
        while (contents.hasNext()){
            deleteContents(contents.next().getId());
        }
    }
}
