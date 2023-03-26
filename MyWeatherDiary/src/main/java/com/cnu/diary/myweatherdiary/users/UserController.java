package com.cnu.diary.myweatherdiary.users;
//pswd
import com.cnu.diary.myweatherdiary.jwt.JwtAuthentication;
import com.cnu.diary.myweatherdiary.jwt.JwtAuthenticationToken;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private final UserService userService;

    @Autowired
    private final AuthenticationManager authenticationManager;

    
    //유저 생성(다이어리 타이틀 받음)
    @PostMapping("/register")
    public UserResponseDto register(@RequestBody UserResponseDto userResponseDto) {
        return userService.register(userResponseDto);
    }


    //새로운 키로 변경: userId 필요.
    @PutMapping("/changeKey")
    public UserResponseDto changeKey(@RequestBody UserResponseDto userResponseDto){
        return userService.changeKey(userResponseDto);
    }

    //유저 정보 수정
    @PutMapping("/update")
    public UserResponseDto updateUserInfo(@RequestBody UserResponseDto userResponseDto){
        return userService.updateUserInfo(userResponseDto);
    }


    // 유저 삭제
    @DeleteMapping("/remove")
    public void removeUser(@RequestBody UserResponseDto userResponseDto){
        userService.removeUser(userResponseDto.getId());
    }

    /**
     * 사용자 로그인
     */
    @PostMapping(path = "/auth/login")
    public LoginDto login(@RequestBody LoginRequest request) {
        JwtAuthenticationToken authToken = new JwtAuthenticationToken(request.getPrincipal(), request.getCredentials());
        Authentication resultToken = authenticationManager.authenticate(authToken);
        JwtAuthentication authentication = (JwtAuthentication) resultToken.getPrincipal();

        return new LoginDto(authentication.token, authentication.username, Group.USER.toString());
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
