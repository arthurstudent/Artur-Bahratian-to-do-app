package com.testtask.userservice.service.user;

import com.testtask.userservice.model.dto.request.LoginRequestDto;
import com.testtask.userservice.model.dto.request.RegistrationRequestDto;
import com.testtask.userservice.model.dto.response.ResponseDto;
import lombok.NonNull;

public interface UserService {
    ResponseDto login(LoginRequestDto loginRequestDto);

    void register(RegistrationRequestDto registrationRequestDto);

    boolean existsById(@NonNull Long id);
}
