package com.cnu.diary.myweatherdiary.pswd;
//pswd
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.NoSuchAlgorithmException;
import java.util.UUID;


@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class PswdController {
    @Autowired
    PswdService pswdService;

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
        return pswdService.createNew(pswdEntity);
    }

    @PostMapping("/update/{id}")
    public PswdEntity update(@PathVariable("id") long id){
        return pswdService.getInfo(id);
    }

    @GetMapping("/getAllInfo")
    public Iterable<PswdEntity> findAll(){
        return pswdService.findAll();
    }

    @GetMapping("/login")
    public void login(PswdEntity pswdEntity, HttpSession session){
        String sessionId = UUID.randomUUID().toString();
        session.setAttribute(sessionId, pswdService.login(pswdEntity));
    }

}
