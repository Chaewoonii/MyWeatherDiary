package com.cnu.diary.myweatherdiary.daily.content;

import com.cnu.diary.myweatherdiary.daily.post.Post;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class ContentService {

    @Autowired
    ContentRepository contentRepository;

    @Autowired
    ContentImgHandler contentImgHandler;

    public ContentDto convertContentToDto(Content content) throws IOException{
        ContentDto contentDto = new ContentDto();
        contentDto.setId(content.getId());
        contentDto.setComment(content.getComment());
        contentDto.setImg(
                contentImgHandler.getBase64ImgFromLocal(content.getImgName())
        );
        return contentDto;
    }
    @Transactional
    @PostMapping("contents")
    public List<ContentDto> saveContents(List<ContentDto> contentDtos) throws IOException{
        List<ContentDto> contentRequestDtoList = new ArrayList<>();
        Iterator<ContentDto> iterator = contentDtos.iterator();

        while(iterator.hasNext()){
            ContentDto dto = iterator.next();
            Content content = Content.builder()
                    .comment(dto.getComment())
                    .prefix(Prefix.develop)
                    .imgName(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS).toString())
                    .build();

            Content saved = contentRepository.save(content);
            contentRequestDtoList.add(convertContentToDto(saved));
            contentImgHandler.saveContentsImageLocal(dto.getImg(), saved.getImgName());
        }

        return contentRequestDtoList;
    }



    @Transactional
    @PostMapping("contents")
    public List<ContentDto> updateContents(List<ContentDto> contentDtos) throws IOException{
        List<ContentDto> contentList = new ArrayList<>();
        Iterator<ContentDto> iterator = contentDtos.iterator();

        while (iterator.hasNext()){
            ContentDto dto = iterator.next();
            Content content = Content.builder()
                    .id(dto.getId())
                    .comment(dto.getComment())
                    .imgName(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS).toString())
                    .build();

            Content saved = contentRepository.save(content);
            contentList.add(convertContentToDto(saved));
            contentImgHandler.saveContentsImageLocal(dto.getImg(), saved.getImgName());
        }
        return contentList;
    }

    public List<ContentDto> findByPostId(UUID id) throws IOException{
        List<ContentDto> contentDtos = new ArrayList<>();
        Iterator<Content> iterator = contentRepository.findAllByPost_Id(id).iterator();

        while (iterator.hasNext()){
            ContentDto contentDto = convertContentToDto(iterator.next());
            contentDtos.add(contentDto);
        }
        return contentDtos;
    }


}
