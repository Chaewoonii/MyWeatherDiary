package com.cnu.diary.myweatherdiary.users;
//pswd
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public User register(@RequestBody UserDto userDto) {
        return userService.register(userDto);
    }


    //새로운 키로 변경: userId 필요.
    @PutMapping("/changeKey")
    public User changeKey(@RequestBody UserDto userDto){
        return userService.changeKey(userDto);
    }

    //유저 정보 수정
    @PutMapping("/update")
    public User updateUserInfo(@RequestBody UserDto userDto){
        return userService.updateUserInfo(userDto);
    }


    // 유저 삭제
    @DeleteMapping("/remove")
    public void removeUser(@RequestBody UserDto userDto){
        userService.removeUser(userDto.getId());
    }

    //로그인
    @PostMapping("/login")
    public HttpStatus login(@RequestBody UserDto userDto, HttpSession session){
        Optional<String> id = userService.login(userDto);
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
