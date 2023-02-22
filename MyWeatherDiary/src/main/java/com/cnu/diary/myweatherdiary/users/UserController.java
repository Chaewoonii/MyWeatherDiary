package com.cnu.diary.myweatherdiary.users;
//pswd
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class UserController {
    @Autowired
    UserService userService;
    
    //유저 생성(다이어리 타이틀 받음)
    @PostMapping("/register")
    public UserEntity register(@RequestBody UserEntity userEntity) {
        return userService.register(userEntity);
    }

    //다이어리 타이틀 수정
    @PutMapping("/modifyTitle")
    public UserEntity modifyTitle(@RequestBody UserEntity userEntity){
        return userService.modifyTitle(userEntity);
    }

    //새로운 키로 변경
    @PutMapping("/modifyAuthKey")
    public UserEntity modifyAuthKey(@RequestBody UserEntity userEntity){
        return userService.saveWithNewKey(userEntity);
    }

    //로그인
    @PostMapping("/login")
    public UserEntity login(@RequestBody UserEntity userEntity){
        UUID id = userService.login(userEntity);
        return userService.findById(id); //2번 검색해야함.. 오류 어떻게 관리??
    }

    // 유저 삭제
    @DeleteMapping("/remove/{id}")
    public void removeUser(@PathVariable("id") UUID id){
        userService.removeUser(id);
    }
    /*
    //로그인(pw입력)
    @PostMapping("/login")
    public Long login(UserEntity userEntity){
        return userService.login(userEntity);
    }


    @GetMapping("/getAllInfo")
    public Iterable<PswdEntity> findAll(){
        return pswdService.findAll();
    }
    */
}
