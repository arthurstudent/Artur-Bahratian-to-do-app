package com.testtask.userservice.service.user.impl;

import com.testtask.userservice.dao.UserDaoService;
import com.testtask.userservice.exceptions.custom.IncorrectPasswordException;
import com.testtask.userservice.exceptions.custom.UserAlreadyExistsException;
import com.testtask.userservice.exceptions.custom.UserIsNotFoundException;
import com.testtask.userservice.model.dto.request.LoginRequestDto;
import com.testtask.userservice.model.dto.request.RegistrationRequestDto;
import com.testtask.userservice.model.dto.response.ResponseDto;
import com.testtask.userservice.model.entity.UserEntity;
import com.testtask.userservice.service.user.UserService;
import com.testtask.userservice.service.jwt.JwtTokenProvider;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public final class UserServiceImpl implements UserService {

    private final UserDaoService userDaoService;

    private final JwtTokenProvider jwtTokenProvider;

    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseDto login(@NonNull LoginRequestDto loginRequestDto) {
        String email = loginRequestDto.getEmail();

        UserEntity userEntity = Optional.ofNullable(userDaoService.findByEmail(email)).
                orElseThrow(() -> {
                    log.warn("User with email - {} is not found", email);
                    return new UserIsNotFoundException("User with email - %s is not found. Please provide another email or register current".formatted(email));
                });

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), userEntity.getPassword())) {
            log.warn("Wrong password provided by user with email - {}", email);
            throw new IncorrectPasswordException("Wrong password provided");
        }

        Long id = userEntity.getId();

        String generatedToken = jwtTokenProvider.generateToken(String.valueOf(id));
        log.info("Token generated for user with email after login - {}", id);
        return new ResponseDto(generatedToken);
    }

    @Override
    public void register(@NonNull RegistrationRequestDto registrationRequestDto) {
        if (userDaoService.existsByEmail(registrationRequestDto.getEmail())) {
            throw new UserAlreadyExistsException("User with email - %s already exists".formatted(registrationRequestDto.getEmail()));
        }

        UserEntity userEntity = new UserEntity().setEmail(registrationRequestDto.getEmail())
                .setPassword(passwordEncoder.encode(registrationRequestDto.getPassword()))
                .setFirstName(registrationRequestDto.getFirstName())
                .setLastName(registrationRequestDto.getLastName());

        userDaoService.save(userEntity);
    }

    @Override
    public boolean existsById(@NonNull Long id) {
        return userDaoService.existsById(id);
    }
}
