package com.cnu.diary.myweatherdiary.test;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class MainService {

    @Autowired
    private final PswdRepository pswdRepo;

    @Transactional
    @PostMapping("tbl_pswd")
    public PswdEntity createNew(PswdEntity pswdEntity) throws NoSuchAlgorithmException {
        PswdEncoder pswdEncoder = new PswdEncoder(pswdEntity.getPswd());
        return pswdRepo.save(new PswdEntity(pswdEncoder.getHex()));
    }

    @Transactional
    @GetMapping("tbl_pswd")
    public Iterable<PswdEntity> findAll(){
        return pswdRepo.findAll();
    }
}
