package com.cnu.diary.myweatherdiary.users;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    public PasswordEncoder passwordEncoder;

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

    //유저 생성(다이어리 키 생성, db 자장)
    public UserResponseDto register(UserResponseDto userResponseDto){
        User user = new User();
//        user.setId(UUID.randomUUID());
        user.setDiaryTitle(userResponseDto.getDiaryTitle());
        user.setNickName(new NickNameCreator().getNickName());

        String key = new AuthorizationKeyCreator().getRandomString(20);
        user.setEnterKey(key);
        User saved = userRepository.save(user);
        return userToDto(user);
    }

    //키를 생성해서 저장
    //유저 생성 및 키만 변경 시 사용
    @Transactional
    @PostMapping("users")
    public UserResponseDto changeKey(UserResponseDto userResponseDto) {
        User user = userRepository.findById(userResponseDto.getId()).orElseThrow(
                () -> new NoSuchElementException(User.class.getPackageName())
        );

        String key = new AuthorizationKeyCreator().getRandomString(20);

        user.setEnterKey(key);
        User saved = userRepository.save(user);
        return userToDto(saved);
    }

    //유저 수정
    @Transactional
    @PostMapping("users")
    public UserResponseDto updateUserInfo(UserResponseDto userResponseDto){
        User user = userRepository.findById(userResponseDto.getId()).orElseThrow(
                () -> new NoSuchElementException(User.class.getPackageName())
        );
        user.setDiaryTitle(userResponseDto.getDiaryTitle());
        if (userResponseDto.getEmail().isPresent()){
            user.setEmail(user.getEmail());
        }
        user.setNickName(userResponseDto.getNickName());

        User saved = userRepository.save(user);
        return userToDto(saved);
    }


    //id로 유저 찾기
    @Transactional
    @GetMapping("users")
    public UserResponseDto findById(UUID id) {
        User user = userRepository.findById(id).orElseThrow();
        return userToDto(user);
    }

    //유저 삭제
    @Transactional
    @PostMapping("users")
    public void removeUser(UUID id) {
        userRepository.deleteById(id);
    }

    //로그인(username과 비밀번호)
    @Transactional
    public User login(UUID uuid, String credentials) {
        checkArgument(isNotEmpty(uuid.toString()), "principal must be provided.");
        checkArgument(isNotEmpty(credentials), "credentials must be provided.");

        User user = userRepository.findById(uuid)
                .orElseThrow(() -> new UsernameNotFoundException("Could not found user for " + uuid));
        user.checkPassword(passwordEncoder, credentials);
        return user;
    }

    @Transactional
    @GetMapping("users")
    public List<UserResponseDto> findAll() {
        List<UserResponseDto> users = new ArrayList<>();
        Iterable<User> all = userRepository.findAll();
        all.forEach((u)->users.add(userToDto(u)));
        return users;
    }


    /*
    @Transactional
    @GetMapping("tbl_pswd")
    public Iterable<UserEntity> findAll(>){
        return pswdRepo.findAll();
    }

    @Transactional
    @PostMapping("tbl_pswd")
    public UserEntity getInfo(UUID id) {
        return pswdRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
    }

    public Long login(UserEntity UserEntity) {
        Optional<Long> id = Optional.ofNullable(pswdRepo.authKey(UserEntity.getPswd()));
        return id.orElseThrow(() -> new IllegalArgumentException("Invalid key"));
    }*/
}
