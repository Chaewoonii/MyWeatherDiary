package com.cnu.diary.myweatherdiary.users.dto;

import com.cnu.diary.myweatherdiary.users.domain.GroupPermission;
import com.cnu.diary.myweatherdiary.users.domain.UserGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
    private String username;
    private UserGroup userGroup;
    private List<GroupPermission> groupPermissions;

    public List<GrantedAuthority> getAuthorities(){
        return groupPermissions.stream()
                .map(gp -> new SimpleGrantedAuthority(gp.getPermission().getName()))
                .collect(Collectors.toList());
    }
}
