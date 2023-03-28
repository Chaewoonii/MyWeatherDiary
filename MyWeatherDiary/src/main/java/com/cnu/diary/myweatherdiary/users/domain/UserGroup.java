package com.cnu.diary.myweatherdiary.users.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Entity
@Table(name = "user_groups")
@AllArgsConstructor
@NoArgsConstructor
public class UserGroup {

    @Id
    @Column
    private Long id;

    @Column
    private String name;

    @OneToMany(mappedBy = "userGroup", cascade = CascadeType.PERSIST)
    private List<GroupPermission> permissions;

    public void setPermissions(List<GroupPermission> groupPermissions){
        this.permissions = groupPermissions;
    }

    public List<GrantedAuthority> getAuthorities(){
        return permissions.stream()
                .map(gp -> new SimpleGrantedAuthority(gp.getPermission().getName()))
                .collect(Collectors.toList());
    }

}
