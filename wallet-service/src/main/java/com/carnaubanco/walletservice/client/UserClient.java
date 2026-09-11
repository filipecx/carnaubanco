package com.carnaubanco.walletservice.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient (name = "user-service", url = "${clients.user-service.url:http://localhost:8081}")
public interface UserClient {

    @GetMapping ("/api/v1/users/{id}/active")
    Boolean isUserActive(@PathVariable("id") UUID id);

}
