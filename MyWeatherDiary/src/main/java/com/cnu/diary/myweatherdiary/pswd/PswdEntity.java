package com.cnu.diary.myweatherdiary.pswd;

import jakarta.persistence.*;
import lombok.*;

@Entity(name="tbl_pswd")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PswdEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String pswd;

    public PswdEntity(String pswd){
        this.pswd = pswd;
    }

}