package com.cnu.diary.myweatherdiary.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PostNotFoundException extends RuntimeException{
    public PostNotFoundException(String msg){
        super(msg);
    }

    public PostNotFoundException(RuntimeException e){
        super(e);
    }
}
