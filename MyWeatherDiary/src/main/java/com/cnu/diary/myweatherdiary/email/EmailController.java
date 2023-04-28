package com.cnu.diary.myweatherdiary.email;

import com.cnu.diary.myweatherdiary.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.IOException;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {



    public final GmailService gmailService;

    @PostMapping("")
    public ApiResponse<String> sendMail(@RequestBody EmailDto emailDto) throws MessagingException, IOException {
//        gmailService.printLabel("MyWeatherDiary");
        gmailService.sendEnterKey(emailDto.getReceiver(), emailDto.getDiaryTitle(), emailDto.getEnterKey());
        return ApiResponse.ok("send email success: " + emailDto.getReceiver());
    }
}
