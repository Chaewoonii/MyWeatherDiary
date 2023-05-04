package com.cnu.diary.myweatherdiary.diary.content;

import com.cnu.diary.myweatherdiary.aws.AwsS3Service;
import com.cnu.diary.myweatherdiary.diary.post.Post;
import com.cnu.diary.myweatherdiary.exception.ContentNotFoundException;
import com.cnu.diary.myweatherdiary.exception.ImgNotFoundException;
import com.cnu.diary.myweatherdiary.utill.EntityConverter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContentS3Service {

    private final ContentRepository contentRepository;

    private final AwsS3Service awsS3Service;

    private final EntityConverter entityConverter;

    @Transactional
    @PostMapping("contents")
    public List<ContentDto> saveContents(List<ContentDto> contentDtos, Post post) throws IOException {
        List<ContentDto> contentRequestDtoList = new ArrayList<>();
        Iterator<ContentDto> iterator = contentDtos.iterator();

        while(iterator.hasNext()){
            ContentDto dto = iterator.next();
            Content content = Content.builder()
                    .comment(dto.getComment())
                    .contentOrder(dto.getContentOrder())
                    .prefix(Prefix.develop)
                    .post(post)
                    .build();

            Content saved = contentRepository.save(content);
            if (dto.getImg().isPresent()){
                String imgName = content.getId().toString();
                awsS3Service.uploadFile("img/png", imgName, dto.getImg().orElseThrow(() -> new ImgNotFoundException("이미지 없음")));
//                log.info("save success: {}", imgName);
            }

            contentRequestDtoList.add(entityConverter.convertContentToDtoWithOutImg(saved));
        }

        return contentRequestDtoList;
    }

    @Transactional
    @PostMapping("contents")
    public List<ContentDto> updateContents(List<ContentDto> contentDtos, Post post) throws IOException{
        deleteAllContentsByPostId(post.getId());
        List<ContentDto> contentList = new ArrayList<>();
        Iterator<ContentDto> iterator = contentDtos.iterator();

        while (iterator.hasNext()){
            ContentDto dto = iterator.next();
            Content content = Content.builder()
                    .comment(dto.getComment())
                    .prefix(Prefix.develop)
                    .contentOrder(dto.getContentOrder())
                    .post(post)
                    .build();

            Content saved = contentRepository.save(content);
            ContentDto contentDto = entityConverter.convertContentToDtoWithOutImg(saved);
            if (dto.getImg().isPresent()){
                String imgName = content.getId().toString();
                awsS3Service.uploadFile("img/png", imgName, dto.getImg().orElseThrow(() -> new ImgNotFoundException("이미지 없음")));
//                log.info("upload: save success: {}", imgName);

                contentDto.setImg(
                        Optional.of(awsS3Service.getImgBytesFromS3(imgName))
                );
            }

            contentList.add(contentDto);
        }

        return contentList;
    }

    public ContentDto findById(UUID id) {
        Content content = contentRepository.findById(id).orElseThrow(ContentNotFoundException::new);
        ContentDto contentDto = entityConverter.convertContentToDtoWithOutImg(content);
        contentDto.setImg(
                Optional.ofNullable(awsS3Service.getImgBytesFromS3(id.toString()))
        );
        return contentDto;
    }

    public List<ContentDto> findByPostId(UUID id){
        List<ContentDto> contentDtos = new ArrayList<>();
        Iterator<Content> iterator = contentRepository.findAllByPostId(id).iterator();

        while (iterator.hasNext()){
            Content content = iterator.next();
            ContentDto contentDto = entityConverter.convertContentToDtoWithOutImg(content);
            contentDtos.add(contentDto);
            Optional<String> imgData = Optional.ofNullable(awsS3Service.getImgBytesFromS3(content.getId().toString()));
//            log.info("img data : {}", imgData);
            contentDto.setImg(
                    imgData
            );
//            log.info("founded img name: {}", content.getId().toString());
        }

        return contentDtos;
    }

    public void deleteContents(UUID id) throws ImgNotFoundException{
        contentRepository.deleteById(id);
        awsS3Service.deleteImg(id.toString());
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

/*    public void deleteAllImgByPostId(UUID postId){
        Iterator<Content> contents = contentRepository.findAllByPostId(postId).iterator();
        while (contents.hasNext()){
            UUID id = contents.next().getId();
            try {
                contentRepository.deleteById(id);
                awsS3Service.deleteImg(id.toString());
                log.info("deleted Img -> name: {}", id);
            }catch (ImgNotFoundException e){
                e.getMessage();
                log.warn("delete image failed -> name: {}", id);
            }
        }
    }*/

    public String getImgBase64FromId(UUID contentId){
        return awsS3Service.getImgBytesFromS3(contentId.toString());
    }


}

