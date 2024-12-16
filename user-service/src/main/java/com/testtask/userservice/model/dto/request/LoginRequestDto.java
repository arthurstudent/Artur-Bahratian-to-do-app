package com.testtask.userservice.model.dto.request;

import com.testtask.userservice.validation.annotation.ValidEmail;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDto {

    @NotBlank(message = "Email cannot be blank")
    @ValidEmail
    private String email;

    @NotBlank(message = "Password cannot be blank")
    private String password;
}
