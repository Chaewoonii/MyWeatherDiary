package com.cnu.diary.myweatherdiary.users;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    //유저 생성(다이어리 키 생성, db 자장)
    //키를 변경할 때도 사용
    @Transactional
    @PostMapping("users")
    public UserEntity saveWithNewKey(UserEntity userEntity) {
        String key = new AuthorizationKeyCreator(new StringBuffer(), new SecureRandom()).getRandomString(20);
        userEntity.setEnter_key(key);
        return userRepository.save(userEntity);
    }

    //다이어리 타이틀 변경 시 사용.
    @Transactional
    @PutMapping("users")
    public UserEntity save(UserEntity userEntity){
        return userRepository.save(userEntity);
    }

    @Transactional
    @PostMapping("users")
    public UserEntity login(UserEntity userEntity) {
        return userRepository.authKey(userEntity.getEnter_key());
    }

    /*
    @Transactional
    @GetMapping("tbl_pswd")
    public Iterable<UserEntity> findAll(){
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
