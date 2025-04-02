package com.myweatherdiary.v2.controller;

import com.myweatherdiary.v2.domain.diary.DiaryDto;
import com.myweatherdiary.v2.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/diary")
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping("/register")
    public String createDiary(@RequestBody DiaryDto request){
        return diaryService.register(request.getDiaryTitle()); // 아이디_비밀번호
    }

    @GetMapping("")
    public DiaryDto findDiary(@RequestBody DiaryDto request){
        return diaryService.findDiary(request.getId());
    }

}
