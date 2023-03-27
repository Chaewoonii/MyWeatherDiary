package com.cnu.diary.myweatherdiary.users.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@Getter
@Entity
@Table(name = "user_groups")
public class UserGroup {

    @Id
    @Column
    private Long id;

    @Column
    private String name;

    @OneToMany(mappedBy = "userGroup")
    private List<GroupPermission> permissions = new ArrayList<>();

    public List<GrantedAuthority> getAuthorities(){
        return permissions.stream()
                .map(gp -> new SimpleGrantedAuthority(gp.getPermission().getName()))
                .collect(Collectors.toList());
    }

}
