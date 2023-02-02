package com.cnu.diary.myweatherdiary.test;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class MainController {
    @Autowired
    MainService mainService;

    @RequestMapping("/")
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(new PswdEntity());
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @PostMapping("/createNew")
    public PswdEntity createNew(PswdEntity pswdEntity) throws RuntimeException, NoSuchAlgorithmException {
        System.out.println("createNew init");
        return mainService.createNew(pswdEntity);
    }

}
