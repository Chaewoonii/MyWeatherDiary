package com.cnu.diary.myweatherdiary;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/docs")
public class DocsController {

    @GetMapping("/user")
    public String userDocs(){
        return "/docs/user.html";
    }

    @GetMapping("/post")
    public String postDocs(){
        return "/docs/post.html";
    }
}
