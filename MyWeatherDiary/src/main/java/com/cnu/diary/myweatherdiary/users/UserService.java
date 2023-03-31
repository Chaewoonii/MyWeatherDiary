package com.cnu.diary.myweatherdiary.users;

import com.cnu.diary.myweatherdiary.jwt.JwtAuthenticationToken;
import com.cnu.diary.myweatherdiary.users.domain.*;
import com.cnu.diary.myweatherdiary.users.dto.UserRegisterDto;
import com.cnu.diary.myweatherdiary.users.dto.UserRequestDto;
import com.cnu.diary.myweatherdiary.users.dto.UserResponseDto;
import com.cnu.diary.myweatherdiary.users.repository.UserRepository;
import com.cnu.diary.myweatherdiary.users.utill.AuthorizationKeyCreator;
import com.cnu.diary.myweatherdiary.utill.EntityConverter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final EntityConverter entityConverter;

    private final PasswordEncoder passwordEncoder;


    public UserGroup getUserGroup(Long role){
        if (role == 1L){
            Permission permission = new Permission(role, Role.ROLE_USER.toString());
            UserGroup userGroup = new UserGroup(role, Group.USER_GROUP.toString(), new ArrayList<>());
            GroupPermission groupPermission = new GroupPermission(role, userGroup, permission);
            userGroup.getPermissions().add(groupPermission);

            return userGroup;
        }else {
            Permission permission1 = new Permission(role, Role.ROLE_USER.toString());
            Permission permission2 = new Permission(role, Role.ROLE_ADMIN.toString());
            UserGroup adminGroup = new UserGroup(role, Group.ADMIN_GROUP.toString(), new ArrayList<>());
            GroupPermission groupPermission1 = new GroupPermission(role, adminGroup, permission1);
            GroupPermission groupPermission2 = new GroupPermission(role, adminGroup, permission2);
            adminGroup.getPermissions().add(groupPermission1);
            adminGroup.getPermissions().add(groupPermission2);

            return adminGroup;
        }

    }

    //유저 생성(다이어리 키 생성, db 자장)
    @Transactional
    public UserResponseDto register(UserRegisterDto userRegisterDto){
        String key = new AuthorizationKeyCreator().getRandomString(20);

        UserGroup userGroup = getUserGroup(userRegisterDto.getRole());

        User user = entityConverter.createUser(
                userRegisterDto,
                passwordEncoder.encode(key),
                userGroup);
        User saved = userRepository.save(user);
        log.info("saved -> {}", saved);
        return entityConverter.getUserDto(saved, key);
    }

    //키를 생성해서 저장
    //유저 생성 및 키만 변경 시 사용
    @Transactional
    public UserResponseDto changeKey(UUID id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException(User.class.getPackageName())
        );
        String key = new AuthorizationKeyCreator().getRandomString(20);

        User updated = entityConverter.updateUserKey(user,
                passwordEncoder.encode(key));

        User saved = userRepository.save(updated);
        return entityConverter.getUserDto(saved, key);
    }

    //유저 수정
    @Transactional
    public UserResponseDto updateUserInfo(UUID id, UserRequestDto userRequestDto){
        User user = userRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException(User.class.getPackageName())
        );
        User updated = entityConverter.updateUser(user, userRequestDto);

        User saved = userRepository.save(updated);
        return entityConverter.getUserDto(saved);
    }


    //id로 유저 찾기
    @Transactional
    public UserResponseDto findById(UUID id) {
        User user = userRepository.findById(id).orElseThrow();
        return entityConverter.getUserDto(user);
    }

    //유저 삭제
    @Transactional
    public void removeUser(UUID id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public List<UserResponseDto> findAll() {
        List<UserResponseDto> users = new ArrayList<>();
        Iterable<User> all = userRepository.findAll();
        all.forEach((u)->users.add(entityConverter.getUserDto(u)));
        return users;
    }


    @Transactional
    public Authentication authenticate(JwtAuthenticationToken authToken) {
        Authentication resultToken = authenticationManager.authenticate(authToken);
        return resultToken;
    }



}
