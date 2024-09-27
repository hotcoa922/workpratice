package com.example.microserviceboard.client;

import com.example.microserviceboard.config.FeignClientConfig;
import com.example.microserviceboard.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "MicroServiceUser", url = "http://localhost:5001", configuration = FeignClientConfig.class)
public interface UserServiceClient {

    @GetMapping("/auth/me")
    UserDto getAuthenticatedUser(@RequestHeader("Authorization") String token);
}
