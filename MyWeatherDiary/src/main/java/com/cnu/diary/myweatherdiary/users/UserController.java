package com.cnu.diary.myweatherdiary.users;
//pswd
import com.cnu.diary.myweatherdiary.ApiResponse;
import com.cnu.diary.myweatherdiary.jwt.JwtAuthentication;
import com.cnu.diary.myweatherdiary.jwt.JwtAuthenticationToken;
import com.cnu.diary.myweatherdiary.users.domain.MappedKey;
import com.cnu.diary.myweatherdiary.users.dto.*;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final MappedKeyService mappedKeyService;

    /**
     * username도 난수로 만들어주기!  -> URL로 접근 64 byte String
     * username과 password 백엔드에서 만들어서 저장
     * 사용자는 EnterKey만으로 접근 enterkey에 username, password mapping됨.
     * 로그인 -> post, /user: request: loginDto
     * 로그아웃 -> get, /user/auth request: authentication
     * */

    @PostMapping("")
    public ApiResponse<UserResponseDto> register(@RequestBody UserRegisterDto userRegisterDto) {
        MappedKey entity = mappedKeyService.register();
        UserResponseDto registered = userService.register(entity, userRegisterDto);
        return ApiResponse.ok(registered);
    }

    @PutMapping("/auth/changeKey")
    public ApiResponse<UserResponseDto> changeKey(@RequestBody LoginRequestDto loginRequestDto,
                                     @AuthenticationPrincipal JwtAuthentication authentication){
        this.login(loginRequestDto);
        UserResponseDto userResponseDto = userService.changeKey(authentication.username);
        return ApiResponse.ok(userResponseDto);
    }

    @PutMapping("/auth")
    public ApiResponse<UserResponseDto> updateUserInfo(@RequestBody UserRequestDto userRequestDto,
                                                       @AuthenticationPrincipal JwtAuthentication authentication){
        UserResponseDto userResponseDto = userService.updateUserInfo(authentication.username, userRequestDto);
        return ApiResponse.ok(userResponseDto);
    }

    @GetMapping("/auth")
    public ApiResponse<UserResponseDto> getUser(@AuthenticationPrincipal JwtAuthentication authentication){
        UserResponseDto found = userService.findByUsername(authentication.username);
        return ApiResponse.ok(found);
    }

    @DeleteMapping("/auth")
    public ApiResponse<String> removeUser(@AuthenticationPrincipal JwtAuthentication authentication){
        userService.removeUserByUsername(authentication.username);
        return ApiResponse.ok("username <"+authentication.username+"> Delete Success ");
    }

    @PostMapping(path = "/login")
    public ApiResponse<UserTokenDto> login(@RequestBody LoginRequestDto request) {
        String username = mappedKeyService.findByEnterKey(request.getEnterKey());
        JwtAuthenticationToken authToken = new JwtAuthenticationToken(username, request.getEnterKey());
        Authentication resultToken = userService.authenticate(authToken);
        JwtAuthenticationToken authenticated = (JwtAuthenticationToken) resultToken;
        JwtAuthentication principal = (JwtAuthentication) authenticated.getPrincipal();
        LoginResponseDto loginResponseDto = (LoginResponseDto) resultToken.getDetails();
        UserTokenDto userTokenDto = new UserTokenDto(principal.token, principal.username, loginResponseDto.getUserGroup().getName());
        return ApiResponse.ok(userTokenDto);
    }

}
