package com.cnu.diary.myweatherdiary.users;
//pswd
import com.cnu.diary.myweatherdiary.jwt.JwtAuthentication;
import com.cnu.diary.myweatherdiary.jwt.JwtAuthenticationToken;
import com.cnu.diary.myweatherdiary.users.dto.*;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@AllArgsConstructor
@RequestMapping("/api/user") //버전은 빼기 ->
public class UserController {

    private final UserService userService;

    private final UserDetailService userDetailService;

    /**
     * username도 난수로 만들어주기!  -> URL로 접근 64 byte String
     * username과 password 백엔드에서 만들어서 저장
     * 사용자는 EnterKey만으로 접근 enterkey에 username, password mapping됨.
     * */

    
    //유저 생성(다이어리 타이틀 받음)
    @PostMapping("")
    public UserResponseDto register(@RequestBody UserRegisterDto userRegisterDto) {
        return userService.register(userRegisterDto);
    }


    //새로운 키로 변경: userId 필요.
    //클라이언트가 보내는 것에 따라서 수정인지 key 변경인지??? <-- DTO 분리
    @PutMapping("/auth/changeKey")
    public UserResponseDto changeKey(@RequestBody LoginRequestDto loginRequestDto,
                                     @AuthenticationPrincipal JwtAuthentication authentication){
        userDetailService.login(loginRequestDto.getEmail(), loginRequestDto.getEnterKey());

        return userService.changeKey(UUID.fromString(authentication.username));
    }

    //유저 정보 수정
    @PutMapping("/auth")
    public UserResponseDto updateUserInfo(@RequestBody UserRequestDto userRequestDto,
                                          @AuthenticationPrincipal JwtAuthentication authentication){
//        @AuthenticationPrincipal JwtAuthentication authentication
//        authentication.token, authentication.username
//        username으로 dto 찾아오기 -> 반환.


        return userService.updateUserInfo(UUID.fromString(authentication.username), userRequestDto);

    }

    //유저 정보 불러오기 -> get, /user/auth request: authentication + loginDto
    @GetMapping("/auth")
    public UserResponseDto getUser(
            @RequestBody LoginRequestDto loginRequestDto,
            @AuthenticationPrincipal JwtAuthentication authentication){
        userDetailService.login(loginRequestDto.getEmail(), loginRequestDto.getEnterKey());

        return userService.findById(UUID.fromString(authentication.username));
    }


    // 로그인 -> post, /user: request: loginDto
    //로그아웃 -> get, /user/auth request: authentication


    // 유저 삭제
    @DeleteMapping("/auth")
    public void removeUser(@AuthenticationPrincipal JwtAuthentication authentication){
        userService.removeUser(UUID.fromString(authentication.username));
    }



    //로그인. username:지금은 enter key. 암호화?? 복호화?? <- 아이디 컬럼 새로 만들기...?..
    //diaryTitle + id 슈퍼키로 유저 찾기
    @PostMapping(path = "/login")
    public UserTokenDto login(@RequestBody LoginRequestDto request) {
        JwtAuthenticationToken authToken = new JwtAuthenticationToken(request.getEmail(), request.getEnterKey());
        Authentication resultToken = userService.authenticate(authToken);
        JwtAuthenticationToken authenticated = (JwtAuthenticationToken) resultToken;
        JwtAuthentication principal = (JwtAuthentication) authenticated.getPrincipal();
        LoginResponseDto loginResponseDto = (LoginResponseDto) resultToken.getDetails();
        return new UserTokenDto(principal.token, principal.username, loginResponseDto.getUserGroup().getName());
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
