package com.cnu.diary.myweatherdiary.utill;

import com.cnu.diary.myweatherdiary.users.domain.User;
import com.cnu.diary.myweatherdiary.users.domain.UserGroup;
import com.cnu.diary.myweatherdiary.users.dto.UserRegisterDto;
import com.cnu.diary.myweatherdiary.users.dto.UserRequestDto;
import com.cnu.diary.myweatherdiary.users.dto.UserResponseDto;
import com.cnu.diary.myweatherdiary.users.utill.NickNameCreator;

public class EntityConverter {

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
        dto.setUsername(user.getUsername());
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
                    .diaryTitle(userRequestDto.getDiaryTitle())
                    .nickName(userRequestDto.getNickName())
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


}
