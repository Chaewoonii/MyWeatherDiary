package com.cnu.diary.myweatherdiary.users;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@ToString
@Entity(name="users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", nullable = false, unique = true, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "enter_key", nullable = false, unique = true, length = 20)
    private String enterKey;

    @Column(name = "diary_title", nullable = false, length = 50)
    private String diaryTitle;

    @Column(name = "nick_name", length = 15)
    private String nickName;

    @Column(name = "email", length = 50)
    private String email;

}
