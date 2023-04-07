package com.cnu.diary.myweatherdiary.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ImgNotFoundException extends RuntimeException{
    public ImgNotFoundException(String msg){
        super(msg);
    }

    public ImgNotFoundException(RuntimeException e){super(e);}

    public ImgNotFoundException(Exception e){
        super(e);
    }
}
