package com.cnu.diary.myweatherdiary.diary.content;

import com.cnu.diary.myweatherdiary.diary.post.Post;
import com.cnu.diary.myweatherdiary.exception.ImgNotFoundException;
import com.cnu.diary.myweatherdiary.utill.EntityConverter;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class ContentService {

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private ContentImgHandler contentImgHandler;

    @Autowired
    private EntityConverter entityConverter;

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

            contentRequestDtoList.add(entityConverter.convertContentToDto(saved));
        }

        return contentRequestDtoList;
    }

    @Transactional
    @PostMapping("contents")
    public List<ContentDto> updateContents(List<ContentDto> contentDtos, Post post) throws IOException{
        deleteAllImgByPostId(post.getId()); //콘텐츠 다 삭제하고 다시 저장.
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
            }
            contentList.add(entityConverter.convertContentToDto(saved));
        }

        return contentList;
    }

    public List<ContentDto> findByPostId(UUID id){
        List<ContentDto> contentDtos = new ArrayList<>();
        Iterator<Content> iterator = contentRepository.findAllByPostId(id).iterator();

        while (iterator.hasNext()){
            ContentDto contentDto = entityConverter.convertContentToDto(iterator.next());
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
            try {
                deleteContents(contents.next().getId());
            }catch (ImgNotFoundException e){
                e.getMessage();
            }
        }
    }

    public void deleteAllImgByPostId(UUID postId){
        Iterator<Content> contents = contentRepository.findAllByPostId(postId).iterator();
        while (contents.hasNext()){
            String id = contents.next().getId().toString();
            try {
                String deleted = contentImgHandler.deleteImg(id);
                log.info("deleted Img -> name: {}", deleted);
            }catch (ImgNotFoundException e){
                e.getMessage();
                log.warn("delete image failed -> name: {}", id);
            }
        }
    }

}
