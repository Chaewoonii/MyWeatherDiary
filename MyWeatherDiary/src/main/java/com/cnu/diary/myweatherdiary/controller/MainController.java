package com.cnu.diary.myweatherdiary.controller;

import com.cnu.diary.myweatherdiary.service.MainService;
import com.cnu.diary.myweatherdiary.vo.PswdEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @Autowired
    MainService mainService;

    @GetMapping(value = "/")
    public String index(){
        return "index";
    }

    @GetMapping(value = "/test")
    public String test(){
        return "test";
    }

    @GetMapping(value = "/createNew")
    public String createNew(Model model){
        PswdEntity pswd = mainService.createNew();
        model.addAttribute("pswdEntity", pswd);
        return "createComplete";
    }



}
