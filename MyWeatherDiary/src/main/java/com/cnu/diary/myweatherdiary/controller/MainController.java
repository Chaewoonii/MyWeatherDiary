package com.cnu.diary.myweatherdiary.controller;

import com.cnu.diary.myweatherdiary.service.MainService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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
    public String createNewPage(Model model){
        model.addAttribute(new PasswordDto());
        return "createNew";
    }

    @PostMapping(value = "/createNew")
    public String createNew(@Valid PasswordDto passwordDto, Errors errors){
        if(errors.hasErrors()){
            return "createNew";
        }
        mainService.createNew(passwordDto.getPswd());
        return "redirect:/";
    }

//    @PostMapping(value = "/createNew")
//    public String createNew(Model model){
//        PswdEntity pswd = mainService.createNew();
//        model.addAttribute("pswdEntity", pswd);
//        return "createComplete";
//    }
}
