package com.cnu.diary.myweatherdiary.users.domain;

import com.cnu.diary.myweatherdiary.users.repository.PermissionRepository;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "group_permission")
@AllArgsConstructor
@NoArgsConstructor
public class GroupPermission {
    @Id
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "group_id")
    private UserGroup userGroup;

    @ManyToOne(optional = false)
    @JoinColumn(name = "permission_id")
    private Permission permission;

    public void addPermission(Permission permission){
        this.permission = permission;
    }
}
