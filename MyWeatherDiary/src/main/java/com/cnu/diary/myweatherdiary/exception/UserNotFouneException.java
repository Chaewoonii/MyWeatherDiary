package com.cnu.diary.myweatherdiary.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserNotFouneException extends RuntimeException{
    public UserNotFouneException(String msg){
        super(msg);
    }

    public UserNotFouneException(RuntimeException e){
        super(e);
    }
}
