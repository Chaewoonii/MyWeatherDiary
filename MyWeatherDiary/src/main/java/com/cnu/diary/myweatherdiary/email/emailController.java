package com.cnu.diary.myweatherdiary.email;

import com.cnu.diary.myweatherdiary.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.IOException;

@RestController
@RequestMapping("/email")
public class emailController {

    private static String FROM_EMAIL = "mwd2023cnu@gmail.com";
    private static String SUBJECT = "Hello! It's MyWeatherDiary";
    private static String BODY_TEXT = "Hello";

    @PostMapping("")
    public ApiResponse<String> sendMail(@RequestBody EmailDto emailDto) throws MessagingException, IOException {
        GmailService.sendEmail(FROM_EMAIL, emailDto.getReceiver(), SUBJECT, BODY_TEXT);
        return ApiResponse.ok("send email success: " + emailDto.getReceiver());
    }
}