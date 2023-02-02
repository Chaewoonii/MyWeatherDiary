package com.cnu.diary.myweatherdiary.test;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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
    public PswdEntity createNew(@RequestBody String pswd) throws RuntimeException{
        System.out.println("createNew init");
        PswdEntity pswdEntity = mainService.createNew(new PswdEntity(pswd));
        Optional<PswdEntity> p = Optional.of(pswdEntity);
        if (p.isEmpty()){
            throw new RuntimeException();
        }else{
            return pswdEntity;
        }

    }

}
