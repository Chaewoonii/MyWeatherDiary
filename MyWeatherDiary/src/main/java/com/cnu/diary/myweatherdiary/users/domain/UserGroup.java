package com.cnu.diary.myweatherdiary.users.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;


@ToString
@Builder
@Getter
@Entity
@Table(name = "user_groups")
@AllArgsConstructor
@NoArgsConstructor
public class UserGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column
    private Long id;

    @Column
    private String name;

    @OneToMany(mappedBy = "userGroup")
    private List<GroupPermission> groupPermissions;

    public void setGroupPermissions(List<GroupPermission> groupPermissions){
        this.groupPermissions = groupPermissions;
    }

    public List<GrantedAuthority> getAuthorities(){
        return groupPermissions.stream()
                .map(gp -> new SimpleGrantedAuthority(gp.getPermission().getName()))
                .collect(Collectors.toList());
    }

}
