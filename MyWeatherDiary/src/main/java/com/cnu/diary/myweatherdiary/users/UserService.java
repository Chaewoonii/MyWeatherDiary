package com.cnu.diary.myweatherdiary.users;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    //유저 생성(다이어리 키 생성, db 자장)
    public User register(UserDto userDto){
        User user = new User();
//        user.setId(UUID.randomUUID());
        user.setDiaryTitle(userDto.getDiaryTitle());
        user.setNickName(new NickNameCreator().getNickName());

        String key = new AuthorizationKeyCreator().getRandomString(20);
        user.setEnterKey(key);

        return userRepository.save(user);
    }

    //키를 생성해서 저장
    //유저 생성 및 키만 변경 시 사용
    @Transactional
    @PostMapping("users")
    public User changeKey(UserDto userDto) {
        User user = userRepository.findById(userDto.getId()).orElseThrow(
                () -> new NoSuchElementException(User.class.getPackageName())
        );

        String key = new AuthorizationKeyCreator().getRandomString(20);

        user.setEnterKey(key);
        return userRepository.save(user);
    }

    //유저 수정
    @Transactional
    @PostMapping("users")
    public User updateUserInfo(UserDto userDto){
        User user = userRepository.findById(userDto.getId()).orElseThrow(
                () -> new NoSuchElementException(User.class.getPackageName())
        );
        user.setDiaryTitle(userDto.getDiaryTitle());
        if (userDto.getEmail().isPresent()){
            user.setEmail(user.getEmail());
        }
        user.setNickName(userDto.getNickName());

        return userRepository.save(user);
    }


    //id로 유저 찾기
    @Transactional
    @GetMapping("users")
    public User findById(UUID id) {
        return userRepository.findById(id).orElseThrow();
    }

    //유저 삭제
    @Transactional
    @PostMapping("users")
    public void removeUser(UUID id) {
        userRepository.deleteById(id);
    }

    //로그인
    @Transactional
    @PostMapping("users")
    public Optional<UUID> login(UserDto userDto) {
        return userRepository.findByEnterKey(userDto.getEnterKey());
    }

    //개발용
    @Transactional
    @GetMapping("users")
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
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
