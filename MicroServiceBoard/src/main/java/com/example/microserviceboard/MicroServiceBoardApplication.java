package com.example.microserviceboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MicroServiceBoardApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroServiceBoardApplication.class, args);
    }

}
