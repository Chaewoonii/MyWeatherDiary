package com.cnu.diary.myweatherdiary.test;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Service
@RequiredArgsConstructor
public class MainService {

    private final PswdRepository pswdRepo;

    @PostMapping("tbl_pswd")
    public PswdEntity createNew(PswdEntity pswd){
        return pswdRepo.save(pswd);
    }

    @GetMapping("tbl_pswd")
    public Iterable<PswdEntity> findAll(){
        return pswdRepo.findAll();
    }

}
