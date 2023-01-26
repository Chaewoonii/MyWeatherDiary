package com.cnu.diary.myweatherdiary.vo;

import jakarta.persistence.*;
import lombok.*;

@Entity(name="tbl_pswd")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PswdEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true, length = 32)
    private String pswd;

    public PswdEntity(String pswd){
        this.pswd = pswd;
    }

}
