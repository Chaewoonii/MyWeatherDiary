package com.cnu.diary.myweatherdiary.service;

import com.cnu.diary.myweatherdiary.db.PswdRepository;
import com.cnu.diary.myweatherdiary.vo.PswdEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Service
@Transactional
@RequiredArgsConstructor
public class MainService {
    private final PswdRepository pswdRepo;

    public PswdEntity createNew(String pswd) {
        String encodingPassword = encodingPassword(pswd);
        PswdEntity pswdEntity = PswdEntity.builder()
                .pswd(encodingPassword)
                .build();
        return pswdRepo.save(pswdEntity);
    }

    private static String encodingPassword(String pswd) {
        String hex = "";
        // "SHA1PRNG"은 알고리즘 이름
        SecureRandom random = null;
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
            byte[] bytes = new byte[16];
            random.nextBytes(bytes);

            // SALT 생성
            String salt = new String(Base64.getEncoder().encode(bytes));
            String pswdAndSalt = pswd + salt;
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // 평문+salt 암호화
            md.update(pswdAndSalt.getBytes());
            hex = String.format("%064x", new BigInteger(1, md.digest()));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return hex;
    }

    @GetMapping("tbl_pswd")
    public Iterable<PswdEntity> findAll() {
        return pswdRepo.findAll();
    }

}
