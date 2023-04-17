package com.cnu.diary.myweatherdiary.exception;

import lombok.NoArgsConstructor;

import java.util.function.Supplier;

@NoArgsConstructor
public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(String msg){
        super(msg);
    }

    public PostNotFoundException(RuntimeException e){
        super(e);
    }
}
