package com.cnu.diary.myweatherdiary.users;

import com.cnu.diary.myweatherdiary.exception.UserNotFouneException;
import com.cnu.diary.myweatherdiary.jwt.JwtAuthenticationToken;
import com.cnu.diary.myweatherdiary.users.domain.*;
import com.cnu.diary.myweatherdiary.users.dto.UserRegisterDto;
import com.cnu.diary.myweatherdiary.users.dto.UserRequestDto;
import com.cnu.diary.myweatherdiary.users.dto.UserResponseDto;
import com.cnu.diary.myweatherdiary.users.repository.GroupPermissionRepository;
import com.cnu.diary.myweatherdiary.users.repository.PermissionRepository;
import com.cnu.diary.myweatherdiary.users.repository.UserGroupRepository;
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
    private final UserGroupRepository userGroupRepository;
    private final PermissionRepository permissionRepository;
    private final GroupPermissionRepository groupPermissionRepository;
    private final AuthenticationManager authenticationManager;
    private final EntityConverter entityConverter;

    private final PasswordEncoder passwordEncoder;


    public UserGroup getUserGroup(Long role){
        List<GroupPermission> groupPermissions = new ArrayList<>();
        UserGroup userGroup = userGroupRepository.findById(role).orElse(
                UserGroup.builder()
                        .name(Group.USER_GROUP.toString())
                        .groupPermissions(groupPermissions)
                        .build()
        );

        if (role == 1L){
            Permission permission = permissionRepository.findById(role).orElse(
                    Permission.builder()
                            .name(Role.ROLE_USER.toString())
                            .build()
            );
            permissionRepository.save(permission);

            GroupPermission groupPermission = GroupPermission.builder()
                    .userGroup(userGroup)
                    .build();
            groupPermission.addPermission(permission);
            groupPermissions.add(groupPermission);
            groupPermissionRepository.save(groupPermission);
        }else {
            Long i = 1L;
            while (i <= role){
                Permission permission = permissionRepository.findById(i++).orElse(
                        Permission.builder()
                                .name(Role.ROLE_USER.name())
                                .build()
                );
                permissionRepository.save(permission);

                GroupPermission groupPermission = GroupPermission.builder()
                                .userGroup(userGroup).build();
                groupPermission.addPermission(permission);
                groupPermissions.add(groupPermission);
                groupPermissionRepository.save(groupPermission);
            }
        }
        userGroup.setGroupPermissions(groupPermissions);
        userGroupRepository.save(userGroup);
        return userGroup;
    }

    @Transactional
    public UserResponseDto register(MappedKey mappedKeyEntity, UserRegisterDto userRegisterDto){
        UserGroup userGroup = getUserGroup(userRegisterDto.getRole());

        User user = entityConverter.createUser(
                userRegisterDto,
                mappedKeyEntity.getUsername(),
                passwordEncoder.encode(mappedKeyEntity.getEnterKey()),
                userGroup);

        User saved = userRepository.save(user);
        log.info("saved -> {}", saved);
        return entityConverter.getUserDto(saved, mappedKeyEntity.getEnterKey());
    }

    @Transactional
    public UserResponseDto changeKey(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new NoSuchElementException(User.class.getPackageName())
        );
        String key = new AuthorizationKeyCreator().getRandomString(20);

        User updated = entityConverter.updateUserKey(user,
                passwordEncoder.encode(key));

        User saved = userRepository.save(updated);
        return entityConverter.getUserDto(saved, key);
    }

    @Transactional
    public UserResponseDto updateUserInfo(String username, UserRequestDto userRequestDto){
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new NoSuchElementException(User.class.getPackageName())
        );

        User updated = entityConverter.updateUser(user, userRequestDto);
        User saved = userRepository.save(updated);

        return entityConverter.getUserDto(saved);
    }

    @Transactional
    public UserResponseDto findById(UUID id) {
        User user = userRepository.findById(id).orElseThrow();
        return entityConverter.getUserDto(user);
    }

    @Transactional
    public UserResponseDto findByUsername(String username) {
        return entityConverter.getUserDto(
                userRepository.findByUsername(username).orElseThrow(
                        () -> new UserNotFouneException("Cannot found username <" + username + ">")
                )
        );
    }

    @Transactional
    public void removeUser(UUID id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public void removeUserByUsername(String username){
        userRepository.deleteByUsername(username);
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
