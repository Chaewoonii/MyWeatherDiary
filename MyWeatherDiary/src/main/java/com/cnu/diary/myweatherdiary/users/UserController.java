package com.cnu.diary.myweatherdiary.users;
//pswd
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.Optional;
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

    //새로운 키로 변경 :diary title 6null로 들어옴
    @PutMapping("/modifyAuthKey")
    public UserEntity modifyAuthKey(@RequestBody UserEntity userEntity){
        return userService.saveWithNewKey(userEntity);
    }

    // 유저 삭제
    @DeleteMapping("/remove")
    public void removeUser(@RequestBody UserEntity userEntity){
        userService.removeUser(userEntity.getId());
    }

    //로그인
    @PostMapping("/login")
    public HttpStatus login(@RequestBody UserEntity userEntity, HttpSession session){
        Optional<UUID> id = userService.login(userEntity);
        if (id.isEmpty()){
//            throw new NoSuchElementException(UserController.class.getPackageName());
            return HttpStatus.BAD_REQUEST;
        }else {
            session.setAttribute(UUID.randomUUID().toString(), userService.findById(id.get())); //DB 두번 조회해야함.
            return HttpStatus.ACCEPTED;
        }
    }

    //로그아웃
    @GetMapping("/logout")
    public HttpStatus logout(HttpSession session){
        session.invalidate();
        return HttpStatus.ACCEPTED;
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
