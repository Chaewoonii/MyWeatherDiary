package com.myweatherdiary.v2.controller;

import com.myweatherdiary.v2.domain.diary.DiaryDto;
import com.myweatherdiary.v2.jwt.JwtUtil;
import com.myweatherdiary.v2.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/diary")
public class DiaryController {

    private final DiaryService diaryService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public String createDiary(@RequestBody DiaryDto request){
        return diaryService.register(request.getDiaryTitle()); // 아이디_비밀번호
    }

    @GetMapping("")
    public DiaryDto findDiary(@AuthenticationPrincipal UserDetails token){
        return diaryService.findDiary(token.getUsername());
    }

}
