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
    PswdService mainService;

    @RequestMapping("")
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(new PswdEntity());
        modelAndView.setViewName("auth");
        return modelAndView;
    }

    @PostMapping("/createNew")
    public PswdEntity createNew(PswdEntity pswdEntity) throws RuntimeException, NoSuchAlgorithmException {
        System.out.println("createNew init");
        return mainService.createNew(pswdEntity);
    }

    @PostMapping("/update/{id}")
    public PswdEntity update(@PathVariable("id") long id){
        return mainService.getInfo(id);
    }

    @GetMapping("/getAllInfo")
    public Iterable<PswdEntity> findAll(){
        return mainService.findAll();
    }

}
