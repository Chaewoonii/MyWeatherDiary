package com.cnu.diary.myweatherdiary.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ContentNotFoundException extends RuntimeException{
    public ContentNotFoundException(String msg){
        super(msg);
    }
    public ContentNotFoundException(RuntimeException e){
        super(e);
    }
}
