package com.testtask.userservice.api;

import com.testtask.userservice.model.dto.request.LoginRequestDto;
import com.testtask.userservice.model.dto.request.RegistrationRequestDto;
import com.testtask.userservice.model.dto.response.ResponseDto;
import com.testtask.userservice.model.dto.response.wrapper.ApiResponse;
import com.testtask.userservice.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDto request) {
        ResponseDto login = userService.login(request);
        ApiResponse<ResponseDto> responseDtoApiResponse = ApiResponse.successResponse(login);
        return ResponseEntity.ok(responseDtoApiResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegistrationRequestDto request) {
        userService.register(request);
        ApiResponse<?> apiResponse = ApiResponse.successResponse();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/exists/{userId}")
    public ResponseEntity<ApiResponse<Boolean>> exists(@PathVariable Long userId) {
        boolean existsById = userService.existsById(userId);
        ApiResponse<Boolean> apiResponse = ApiResponse.successResponse(existsById);
        return ResponseEntity.ok(apiResponse);
    }
}
