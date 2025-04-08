package com.myweatherdiary.v2.controller;

import com.myweatherdiary.v2.domain.diary.DiaryDto;
import com.myweatherdiary.v2.jwt.JwtUtil;
import com.myweatherdiary.v2.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.rmi.NoSuchObjectException;

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

    @PutMapping("")
    public DiaryDto updateDiary(@RequestBody DiaryDto request,
                                @AuthenticationPrincipal UserDetails token){
        return diaryService.update(token.getUsername(), request);
    }

    @DeleteMapping("")
    public ResponseEntity<?> removeDiary(@AuthenticationPrincipal UserDetails token) throws NoSuchObjectException {
        diaryService.remove(token.getUsername());
        return ResponseEntity.ok("다이어리가 삭제되었습니다.");
    }
}
