package com.cnu.diary.myweatherdiary.controller;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PasswordDto {

    @Column(nullable = false, unique = true, length = 32)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$", message = "비밀번호는 8~16자 영문 대소문자, 숫자, 특수문자를 사용하세요.")
    private String pswd;
}
