package com.cnu.diary.myweatherdiary;

import com.cnu.diary.myweatherdiary.ApiResponse;
import com.cnu.diary.myweatherdiary.exception.ContentNotFoundException;
import com.cnu.diary.myweatherdiary.exception.ImgNotFoundException;
import com.cnu.diary.myweatherdiary.exception.PostNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler
    public ApiResponse<String> exceptionHandle(Exception exception){
        return ApiResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ImgNotFoundException.class)
    public ApiResponse<String> imgNotFoundExceptionHandle(ImgNotFoundException exception){
        return ApiResponse.fail(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(ContentNotFoundException.class)
    public ApiResponse<String> contentNotFoundExceptionHandle(ContentNotFoundException exception){
        return ApiResponse.fail(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(PostNotFoundException.class)
    public ApiResponse<String> postNotFoundExceptionHandle(PostNotFoundException exception){
        return ApiResponse.fail(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(IOException.class)
    public ApiResponse<String> ioExceptionHandle(IOException exception){
        return ApiResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
    }
}
