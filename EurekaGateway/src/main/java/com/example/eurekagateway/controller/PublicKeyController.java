package com.example.eurekagateway.controller;



import com.example.eurekagateway.util.RsaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rsa")
public class PublicKeyController {

    private RsaUtil rsaUtil;

    @Autowired
    public PublicKeyController(RsaUtil rsaUtil) {
        this.rsaUtil = rsaUtil;
    }


    @GetMapping(value = "/publicKey", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getPublicKey() {
        return rsaUtil.getPublicKey();
    }
}
