package com.cnu.diary.myweatherdiary.users;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.security.SecureRandom;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    //유저 생성(다이어리 키 생성, db 자장)
    public UserEntity register(UserEntity userEntity){
        userEntity.setId(UUID.randomUUID());
        return saveWithNewKey(userEntity);
    }

    //키를 생성해서 저장
    //유저 생성 및 키만 변경 시 사용
    @Transactional
    @PostMapping("users")
    public UserEntity saveWithNewKey(UserEntity userEntity) {
        String key = new AuthorizationKeyCreator(new StringBuffer(), new SecureRandom()).getRandomString(20);
        userEntity.setEnter_key(key);
        return userRepository.save(userEntity);
    }

    //다이어리 타이틀 변경 시 사용.
    @Transactional
    @PostMapping("users")
    public UserEntity save(UserEntity userEntity){
        return userRepository.save(userEntity);
    }


    //id로 유저 찾기
    @Transactional
    @GetMapping("users")
    public UserEntity findById(UUID id) {
        Optional<UserEntity> u = userRepository.findById(id);
        return u.orElseThrow(() -> new NoSuchElementException(UserEntity.class.getPackageName()));
    }

    //타이틀 수정
    @Transactional
    @PutMapping("users")
    public UserEntity modifyTitle(UserEntity userEntity){
        String key = findById(userEntity.getId()).getEnter_key();
        userEntity.setEnter_key(key);
        return userRepository.save(userEntity);
    }

    //로그인: 안됨.
    @Transactional
    @PostMapping("users")
    public UUID login(UserEntity userEntity) {
        Optional<UUID> u = userRepository.findByEnter_key(userEntity.getEnter_key());
        return u.orElseThrow(() -> new NoSuchElementException(UserEntity.class.getPackageName()));
    }

    //유저 삭제
    @Transactional
    @PostMapping("users")
    public void removeUser(UUID id) {
        userRepository.deleteById(id);
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
