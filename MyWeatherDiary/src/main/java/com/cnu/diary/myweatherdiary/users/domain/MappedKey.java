package com.cnu.diary.myweatherdiary.users.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@Table(name = "mapped_keys")
@AllArgsConstructor
@NoArgsConstructor
public class MappedKey {
    @Id
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "enter_key", unique = true, nullable = false)
    private String enterKey;

}
