package com.cnu.diary.myweatherdiary.email;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class GmailService {

    private final GmailSmtp gmailSmtp;

    private static String SITE = "http://myweatherdiary.site";
    private static String SUBJECT = "Hello! Welcome to MyWeatherDiary!";

    public void sendEnterKey(String receiver, String diaryTitle, String enterKey){
        String bodyText = "안녕하세요, MyWeatherDiary 일기장을 등록해주셔서 감사합니다.\n" +
                "당신의 일기 <" + diaryTitle + ">의 비밀 Key는 아래와 같습니다\n"
                + enterKey + "\n" +
                "로그인 시, 위의 Key 를 입력해주세요. 또한, 비밀 Key를 잃어버릴 경우, 복구가 불가능하니 반드시 어딘가에 저장해 주세요.\n" +
                "MyWeatherDiary는 당신만의 동굴입니다. 당신의 세상을 기록해보세요!\n\n"
                + SITE;

        gmailSmtp.sendMail(receiver, SUBJECT, bodyText);
    }
}
