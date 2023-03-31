package com.cnu.diary.myweatherdiary.users.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;


@ToString
@Entity(name="users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", nullable = false, unique = true, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "enter_key", nullable = false, unique = true, length = 80)
    private String enterKey;

    @Column(name = "diary_title", nullable = false, length = 50)
    private String diaryTitle;

    @Column(name = "nick_name", length = 15)
    private String nickName;

    @Column(name = "user_id", length = 50)
    private String userId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "group_id")
    private UserGroup userGroup;

    public void addUserGroup(UserGroup userGroup){
        this.userGroup = userGroup;
    }

    //입력받은 비밀번호(credential, enterKey)와 데이터베이스의 비밀번호를 비교
    public void checkPassword(PasswordEncoder passwordEncoder, String credentials) {
        if (!passwordEncoder.matches(credentials, enterKey))
            throw new IllegalArgumentException("Bad credential");
    }

}
