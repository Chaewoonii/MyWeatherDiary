package com.cnu.diary.myweatherdiary.email;

import com.cnu.diary.myweatherdiary.ApiResponse;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

    private final GmailService gmailService;

    @PostMapping("")
    public ApiResponse<String> sendMail(@RequestBody EmailDto emailDto){
        gmailService.sendEnterKey(emailDto.getReceiver(), emailDto.getDiaryTitle(), emailDto.getEnterKey());
        return ApiResponse.ok("send email success: " + emailDto.getReceiver());
    }
}
