package com.cnu.diary.myweatherdiary.users.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Entity
@Table(name = "permissions")
public class Permission {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;
}
