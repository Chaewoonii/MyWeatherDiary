package com.cnu.diary.myweatherdiary.service;

import com.cnu.diary.myweatherdiary.db.PswdRepository;
import com.cnu.diary.myweatherdiary.vo.PswdEntity;
import jakarta.persistence.Id;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MainService {
    private final PswdRepository pswdRepo;

    @PostMapping("tbl_pswd")
    public PswdEntity createNew(){
        PswdEntity pswd = PswdEntity.builder()
                .pswd("test1234")
                .build();
        return pswdRepo.save(pswd);
    }

    @GetMapping("tbl_pswd")
    public Iterable<PswdEntity> findAll(){
        return pswdRepo.findAll();
    }

}
