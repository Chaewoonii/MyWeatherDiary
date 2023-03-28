package com.cnu.diary.myweatherdiary.utill;

import com.cnu.diary.myweatherdiary.users.domain.User;
import com.cnu.diary.myweatherdiary.users.domain.UserGroup;
import com.cnu.diary.myweatherdiary.users.dto.UserRegisterDto;
import com.cnu.diary.myweatherdiary.users.dto.UserRequestDto;
import com.cnu.diary.myweatherdiary.users.dto.UserResponseDto;
import com.cnu.diary.myweatherdiary.users.utill.NickNameCreator;

import java.util.Optional;

public class EntityConverter {

    public UserResponseDto getUserDto(User user){
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setNickName(user.getNickName());
        dto.setDiaryTitle(user.getDiaryTitle());
        dto.setEnterKey(user.getEnterKey());
        Optional<String> email = Optional.ofNullable(user.getEmail());
        if (email.isPresent()){
            dto.setEmail(email);
        }
        return dto;
    }

    public User createUser(UserRegisterDto userRegisterDto, String key, UserGroup userGroup){
        if (userRegisterDto.getEmail().isPresent()){
            User user = User.builder()
                    .enterKey(key)
                    .diaryTitle(userRegisterDto.getDiaryTitle())
                    .nickName(new NickNameCreator().getNickName())
                    .userGroup(userGroup)
                    .email(userRegisterDto.getEmail().get())
                    .build();
            return user;
        }else{
            User user = User.builder()
                    .enterKey(key)
                    .diaryTitle(userRegisterDto.getDiaryTitle())
                    .nickName(new NickNameCreator().getNickName())
                    .userGroup(userGroup)
                    .build();
            return user;
        }
    }

    public User updateUser(User userInRepo, UserRequestDto userRequestDto){
        if (userRequestDto.getEmail().isPresent()){
            User user = User.builder()
                    .id(userRequestDto.getId())
                    .enterKey(userInRepo.getEnterKey())
                    .diaryTitle(userRequestDto.getDiaryTitle())
                    .nickName(userRequestDto.getNickName())
                    .userGroup(userInRepo.getUserGroup())
                    .email(userRequestDto.getEmail().get())
                    .build();
            return user;
        }else{
            User user = User.builder()
                    .id(userRequestDto.getId())
                    .enterKey(userInRepo.getEnterKey())
                    .diaryTitle(userRequestDto.getDiaryTitle())
                    .nickName(userRequestDto.getNickName())
                    .userGroup(userInRepo.getUserGroup())
                    .build();
            return user;
        }
    }


    public User updateUser(User userInRepo, UserRequestDto userRequestDto, String key){
        if (userRequestDto.getEmail().isPresent()){
            User user = User.builder()
                    .id(userRequestDto.getId())
                    .enterKey(key)
                    .diaryTitle(userRequestDto.getDiaryTitle())
                    .nickName(userRequestDto.getNickName())
                    .userGroup(userInRepo.getUserGroup())
                    .email(userRequestDto.getEmail().get())
                    .build();
            return user;
        }else{
            User user = User.builder()
                    .id(userRequestDto.getId())
                    .enterKey(key)
                    .diaryTitle(userRequestDto.getDiaryTitle())
                    .nickName(userRequestDto.getNickName())
                    .userGroup(userInRepo.getUserGroup())
                    .build();
            return user;
        }
    }


}
