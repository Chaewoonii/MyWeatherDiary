package com.cnu.diary.myweatherdiary.utill;

import com.cnu.diary.myweatherdiary.users.domain.User;
import com.cnu.diary.myweatherdiary.users.dto.UserResponseDto;

import java.util.Optional;

public class ConvertEntityToDto {

    public UserResponseDto userToDto(User user){
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

}
