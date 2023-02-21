package com.cnu.diary.myweatherdiary.users;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.security.SecureRandom;


@Getter
@AllArgsConstructor
public class AuthorizationKeyCreator {

    private String CHAR = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefuhijklmnopqrstuvwxyz0123456789!@#$%^&*";
    private StringBuffer stringBuffer;
    private SecureRandom secureRandom;


    public AuthorizationKeyCreator(StringBuffer stringBuffer, SecureRandom secureRandom){
        this.stringBuffer = stringBuffer;
        this.secureRandom = secureRandom;


    }

    public String getRandomString(int length){
        for(int i=0; i<length; i++){
            int rn = secureRandom.nextInt(CHAR.length());
            this.stringBuffer.append(CHAR.charAt(rn));
        }
        return stringBuffer.toString();
    }


}
