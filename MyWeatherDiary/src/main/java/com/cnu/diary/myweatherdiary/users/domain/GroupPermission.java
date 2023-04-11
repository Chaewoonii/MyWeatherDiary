package com.cnu.diary.myweatherdiary.users.domain;

import com.cnu.diary.myweatherdiary.users.repository.PermissionRepository;
import jakarta.persistence.*;
import lombok.*;


@Builder
@Getter
@Entity
@Table(name = "group_permission")
@AllArgsConstructor
@NoArgsConstructor
public class GroupPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private UserGroup userGroup;

    @ManyToOne
    @JoinColumn(name = "permission_id", referencedColumnName = "id")
    private Permission permission;

    public void addPermission(Permission permission){
        this.permission = permission;
    }
}
