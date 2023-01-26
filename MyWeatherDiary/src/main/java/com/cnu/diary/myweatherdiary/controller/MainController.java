package com.cnu.diary.myweatherdiary.controller;

import com.cnu.diary.myweatherdiary.service.MainService;
import com.cnu.diary.myweatherdiary.vo.PswdEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainController {

    @Autowired
    MainService mainService;

    @RequestMapping(value = "/createNew", method = RequestMethod.POST)
    public String createNew(Model model){
        PswdEntity pswd = mainService.createNew();
        model.addAttribute("pswd", pswd);
        return "createComplete";
    }



}
