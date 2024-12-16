package com.testtask.userservice.model.dto.request;

import com.testtask.userservice.validation.annotation.ValidEmail;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequestDto {

    @NotBlank(message = "Email cannot be blank")
    @ValidEmail
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min= 3, max = 40, message = "Password should contain at least 3 symbols, max 40")
    private String password;

    @NotBlank(message = "Firstname cannot be blank")
    @Size(min = 2, max = 50, message = "Firstname must have at least 2 symbols, max - 50")
    private String firstName;

    @NotBlank(message = "Lastname cannot be blank")
    @Size(min = 2, max = 50, message = "Email must have at least 2 symbols, max - 50")
    private String lastName;
}
