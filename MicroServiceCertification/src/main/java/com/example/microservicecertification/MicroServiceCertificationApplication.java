package com.example.microservicecertification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MicroServiceCertificationApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroServiceCertificationApplication.class, args);
	}

}
