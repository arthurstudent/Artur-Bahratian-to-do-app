package com.testtask.userservice.dao;

import com.testtask.userservice.model.entity.UserEntity;
import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;

public interface UserDaoService {

    void save(@NonNull UserEntity user);

    UserEntity findByEmail(@NonNull String username);

    boolean existsByEmail(@NonNull String email);

    boolean existsById(@NonNull Long userId);
}
