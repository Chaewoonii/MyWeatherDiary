package com.cnu.diary.myweatherdiary.pswd;
//pswd
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.NoSuchAlgorithmException;


@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class PswdController {
    @Autowired
    PswdService pswdService;

    //첫 페이지
    @RequestMapping("")
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(new PswdEntity());
        modelAndView.setViewName("auth");
        return modelAndView;
    }

    //다이어리 생성
    @PostMapping("/create")
    public PswdEntity createDiary(PswdEntity pswdEntity) throws RuntimeException, NoSuchAlgorithmException {
        System.out.println("createNew init");
        return pswdService.createNew(pswdEntity);
    }

    //정보 수정
    @PostMapping("/update/{id}")
    public PswdEntity update(@PathVariable("id") long id){
        return pswdService.getInfo(id);
    }

    //로그인(pw입력)
    @PostMapping("/login")
    public Long login(PswdEntity pswdEntity){
        return pswdService.login(pswdEntity);
    }

    /*
    @GetMapping("/getAllInfo")
    public Iterable<PswdEntity> findAll(){
        return pswdService.findAll();
    }
    */
}
