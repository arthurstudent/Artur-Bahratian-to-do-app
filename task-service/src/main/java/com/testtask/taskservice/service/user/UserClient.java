package com.testtask.taskservice.service.user;

import com.testtask.taskservice.model.dto.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(
        name = "customer-service",
        url = "${application.config.user-service-url}"
)
public interface UserClient {

    @GetMapping("/exists/{userId}")
    Optional<ApiResponse<Boolean>> isUserExists(@PathVariable("userId") Long userId);
}
