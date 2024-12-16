package com.testtask.userservice.dao;

import com.testtask.userservice.model.entity.UserEntity;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@RequiredArgsConstructor
class UserDaoServiceImpl implements UserDaoService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public void save(@NonNull final UserEntity user) {
        UserEntity save = userRepository.save(user);
        log.info("User with email - {} saved", user.getEmail());
    }

    @Override
    public UserEntity findByEmail(@NonNull final String username) {
        return userRepository.findByEmail(username);
    }

    @Override
    public boolean existsByEmail(@NonNull final String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsById(@NonNull final Long userId) {
        return userRepository.existsById(userId);
    }
}
