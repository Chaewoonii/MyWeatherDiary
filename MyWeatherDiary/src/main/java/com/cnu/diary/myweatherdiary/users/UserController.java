package com.cnu.diary.myweatherdiary.users;
//pswd
import com.cnu.diary.myweatherdiary.jwt.JwtAuthentication;
import com.cnu.diary.myweatherdiary.jwt.JwtAuthenticationToken;
import com.cnu.diary.myweatherdiary.users.domain.User;
import com.cnu.diary.myweatherdiary.users.dto.*;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;


    
    //유저 생성(다이어리 타이틀 받음)
    @PostMapping("")
    public UserResponseDto register(@RequestBody UserRegisterDto userRegisterDto) {
        return userService.register(userRegisterDto);
    }


    //새로운 키로 변경: userId 필요.
    @PutMapping("/auth/changeKey")
    public UserResponseDto changeKey(@RequestBody UserRequestDto userRequestDto){
        return userService.changeKey(userRequestDto);
    }

    //유저 정보 수정
    @PutMapping("/auth")
    public UserResponseDto updateUserInfo(@RequestBody UserRequestDto userRequestDto){
        return userService.updateUserInfo(userRequestDto);
    }


    // 유저 삭제
    @DeleteMapping("/auth/remove")
    public void removeUser(@RequestBody UserRequestDto userRequestDto){
        userService.removeUser(userRequestDto.getId());
    }

    /**
     * 사용자 로그인
     */
    @PostMapping(path = "/login")
    public UserTokenDto login(@RequestBody LoginRequestDto request) {
        JwtAuthenticationToken authToken = new JwtAuthenticationToken(request.getEnterKey());
        Authentication resultToken = authenticationManager.authenticate(authToken);
        JwtAuthenticationToken authenticated = (JwtAuthenticationToken) resultToken;
        JwtAuthentication principal = (JwtAuthentication) authenticated.getPrincipal();
        User user = (User) resultToken.getDetails();
        return new UserTokenDto(principal.token, principal.username, user.getUserGroup().getName());

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
