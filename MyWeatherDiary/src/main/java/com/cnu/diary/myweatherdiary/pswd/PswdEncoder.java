package com.cnu.diary.myweatherdiary.pswd;

import lombok.Getter;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;


@Getter
public class PswdEncoder {

    private String pswd;
    public static final String RANDOM_ALGORITHM = "SHA1PRNG";
    public static final String DIGEST_ALGORITHM = "SHA-256";
    public static final String FORMAT = "%064x";
    public SecureRandom secureRandom;
    public MessageDigest messageDigest;


    public PswdEncoder(String pswd) throws NoSuchAlgorithmException {
        this.pswd = pswd;
        this.secureRandom = SecureRandom.getInstance(RANDOM_ALGORITHM);
        this.messageDigest = MessageDigest.getInstance(DIGEST_ALGORITHM);
    }

    public byte[] getBytes(){
        byte[] bytes = new byte[16];
        this.secureRandom.nextBytes(bytes);
        return bytes;
    }

    public String getSalt(byte[] bytes){
        return new String(Base64.getEncoder().encode(bytes));
    }

    public String getHex(){
        String pswdAndsalt = this.pswd + this.getSalt(this.getBytes());
        this.messageDigest.update(pswdAndsalt.getBytes());
        return String.format(FORMAT, new BigInteger(1, this.messageDigest.digest()));
    }

}
