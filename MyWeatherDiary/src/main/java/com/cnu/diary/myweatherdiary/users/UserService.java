package com.cnu.diary.myweatherdiary.users;

import com.cnu.diary.myweatherdiary.users.domain.User;
import com.cnu.diary.myweatherdiary.users.domain.UserGroup;
import com.cnu.diary.myweatherdiary.users.dto.UserRequestDto;
import com.cnu.diary.myweatherdiary.users.dto.UserResponseDto;
import com.cnu.diary.myweatherdiary.users.utill.AuthorizationKeyCreator;
import com.cnu.diary.myweatherdiary.users.utill.NickNameCreator;
import com.cnu.diary.myweatherdiary.utill.ConvertEntityToDto;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Service
@AllArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ConvertEntityToDto convertEntityToDto;

    //유저 생성(다이어리 키 생성, db 자장)
    public UserResponseDto register(UserRequestDto userRequestDto){
        String key = passwordEncoder.encode(
                new AuthorizationKeyCreator().getRandomString(20)
        );

        User user = User.builder()
                .enterKey(key)
                .
                .build();

        User saved = userRepository.save(user);
        return convertEntityToDto.userToDto(saved);
    }

    //키를 생성해서 저장
    //유저 생성 및 키만 변경 시 사용
    @Transactional
    @PostMapping("users")
    public UserResponseDto changeKey(UserRequestDto userRequestDto) {
        User user = userRepository.findById(userRequestDto.getId()).orElseThrow(
                () -> new NoSuchElementException(User.class.getPackageName())
        );

        String key = passwordEncoder.encode(
                new AuthorizationKeyCreator().getRandomString(20)
        );

        user.setEnterKey(key);
        User saved = userRepository.save(user);
        return convertEntityToDto.userToDto(saved);
    }

    //유저 수정
    @Transactional
    @PostMapping("users")
    public UserResponseDto updateUserInfo(UserRequestDto userRequestDto){
        User user = userRepository.findById(userResponseDto.getId()).orElseThrow(
                () -> new NoSuchElementException(User.class.getPackageName())
        );
        user.setDiaryTitle(userResponseDto.getDiaryTitle());
        if (userResponseDto.getEmail().isPresent()){
            user.setEmail(user.getEmail());
        }
        user.setNickName(userResponseDto.getNickName());

        User saved = userRepository.save(user);
        return convertEntityToDto.userToDto(saved);
    }


    //id로 유저 찾기
    @Transactional
    @GetMapping("users")
    public UserResponseDto findById(UUID id) {
        User user = userRepository.findById(id).orElseThrow();
        return convertEntityToDto.userToDto(user);
    }

    //유저 삭제
    @Transactional
    @PostMapping("users")
    public void removeUser(UUID id) {
        userRepository.deleteById(id);
    }

    @Transactional
    @GetMapping("users")
    public List<UserResponseDto> findAll() {
        List<UserResponseDto> users = new ArrayList<>();
        Iterable<User> all = userRepository.findAll();
        all.forEach((u)->users.add(convertEntityToDto.userToDto(u)));
        return users;
    }



}
