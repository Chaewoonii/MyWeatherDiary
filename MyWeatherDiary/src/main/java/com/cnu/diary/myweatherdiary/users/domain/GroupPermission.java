package com.cnu.diary.myweatherdiary.users.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "group_permission")
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

}
