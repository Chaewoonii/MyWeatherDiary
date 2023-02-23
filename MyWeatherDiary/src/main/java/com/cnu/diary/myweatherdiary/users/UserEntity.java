package com.cnu.diary.myweatherdiary.users;

import com.cnu.diary.myweatherdiary.post.PostEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@ToString
@Entity(name="users")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity {

    @Id
    @Column(nullable = false, unique = true)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    @Column(nullable = false, unique = true, length = 20)
    private String enter_key;

    @Column
    private String diary_title;

}
