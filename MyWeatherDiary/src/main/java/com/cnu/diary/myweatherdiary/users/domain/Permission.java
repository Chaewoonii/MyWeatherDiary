package com.cnu.diary.myweatherdiary.users.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Builder
@Getter
@Entity
@Table(name = "permissions")
@NoArgsConstructor
@AllArgsConstructor
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "permission")
    private List<GroupPermission> groupPermission;
}
