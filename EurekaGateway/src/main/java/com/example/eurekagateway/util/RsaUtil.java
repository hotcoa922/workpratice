package com.example.eurekagateway.util;


import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;

import java.security.*;

import java.util.Base64;


/*
 RSA암호화 및 복호화에 필요한 유틸리티 메소드
 */
@Component
public class RsaUtil {

    private KeyPair keyPair;

    //RSA 키 페어 생성
    @PostConstruct
    public void init() throws NoSuchAlgorithmException {
        // RSA 키 페어 생성
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048); // 키 크기 설정
        keyPair = keyPairGenerator.generateKeyPair();
    }

    // 공개 키 반환 (Base64 인코딩)
    public String getPublicKey() {
        PublicKey publicKey = keyPair.getPublic();
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    // 개인 키 반환 (내부에서만 사용)
    private PrivateKey getPrivateKey() {
        return keyPair.getPrivate();
    }

    // 암호화 메서드 (사용하지 않음, 클라이언트에서 사용)
    public String encrypt(String plainText) throws Exception {
        PublicKey publicKey = keyPair.getPublic();
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // 복호화 메서드
    public String decrypt(String encryptedText) throws Exception {
        PrivateKey privateKey = getPrivateKey();
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedBytes, "UTF-8");
    }
}