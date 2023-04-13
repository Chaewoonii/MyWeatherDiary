package com.cnu.diary.myweatherdiary.utill;

import com.cnu.diary.myweatherdiary.diary.content.Content;
import com.cnu.diary.myweatherdiary.diary.content.ContentDto;
import com.cnu.diary.myweatherdiary.diary.content.ContentImgHandler;
import com.cnu.diary.myweatherdiary.diary.post.Post;
import com.cnu.diary.myweatherdiary.diary.post.PostResponseDto;
import com.cnu.diary.myweatherdiary.users.domain.User;
import com.cnu.diary.myweatherdiary.users.domain.UserGroup;
import com.cnu.diary.myweatherdiary.users.dto.UserRegisterDto;
import com.cnu.diary.myweatherdiary.users.dto.UserRequestDto;
import com.cnu.diary.myweatherdiary.users.dto.UserResponseDto;
import com.cnu.diary.myweatherdiary.users.utill.NickNameCreator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EntityConverter {

    @Autowired
    private ContentImgHandler contentImgHandler;

    public UserResponseDto getUserDto(User user, String key){
        UserResponseDto dto = new UserResponseDto();
        dto.setNickName(user.getNickName());
        dto.setDiaryTitle(user.getDiaryTitle());
        dto.setEnterKey(key);
        dto.setUsername(user.getUsername());
        return dto;
    }

    public UserResponseDto getUserDto(User user){
        UserResponseDto dto = new UserResponseDto();
        dto.setNickName(user.getNickName());
        dto.setDiaryTitle(user.getDiaryTitle());
        return dto;
    }

    public User createUser(UserRegisterDto userRegisterDto, String username, String key, UserGroup userGroup){
            User user = User.builder()
                    .enterKey(key)
                    .diaryTitle(userRegisterDto.getDiaryTitle())
                    .nickName(new NickNameCreator().getNickName())
                    .username(username)
                    .userGroup(userGroup)
                    .build();
            return user;
    }

    public User updateUser(User userInRepo, UserRequestDto userRequestDto){
            User user = User.builder()
                    .id(userInRepo.getId())
                    .username(userInRepo.getUsername())
                    .diaryTitle(userRequestDto.getDiaryTitle())
                    .nickName(userRequestDto.getNickName())
                    .enterKey(userInRepo.getEnterKey())
                    .userGroup(userInRepo.getUserGroup())
                    .build();
            return user;
    }


    public User updateUserKey(User userInRepo, String key){
            User user = User.builder()
                    .enterKey(key)
                    .diaryTitle(userInRepo.getDiaryTitle())
                    .nickName(userInRepo.getNickName())
                    .userGroup(userInRepo.getUserGroup())
                    .build();
            return user;
    }


    /*diary - post*/
    public PostResponseDto convertPostToDto(Post post){
        PostResponseDto postResponseDto = new PostResponseDto();
        postResponseDto.setId(post.getId());
        postResponseDto.setEmotion(post.getEmotion());
        postResponseDto.setWrittenDate(post.getWrittenDate());
        postResponseDto.setPostDate(post.getPostDate());

        if (Optional.ofNullable(post.getContents()).isPresent()){
            List<ContentDto> contentDtos = new ArrayList<>();
            post.getContents().forEach(
                    c -> contentDtos.add(convertContentToDto(c))
            );
            postResponseDto.setContents(contentDtos);
        }
        return postResponseDto;
    }

    /*diary - contents*/
    public ContentDto convertContentToDto(Content content){
        ContentDto contentDto = new ContentDto();
        contentDto.setId(content.getId());
        contentDto.setComment(content.getComment());
        contentDto.setImg(
                contentImgHandler.getBase64ImgFromLocal(content.getId().toString())
        );
        return contentDto;
    }

}
