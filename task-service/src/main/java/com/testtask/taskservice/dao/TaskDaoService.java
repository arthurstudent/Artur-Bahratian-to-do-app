package com.testtask.taskservice.dao;

import com.testtask.taskservice.model.dto.TaskFilterDto;
import com.testtask.taskservice.model.entity.TaskEntity;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.data.domain.Page;

public interface TaskDaoService {
    Page<TaskEntity> getAllTasksByUserId(@NonNull TaskFilterDto taskFilterDto);

    TaskEntity getUserTaskById(@NonNull Long id, @NonNull Long userId);

    @Transactional
    void save(TaskEntity task);

    @Transactional
    void deleteUserTask(@NonNull Long id);

    boolean existsUserTaskById(@NonNull Long id, @NonNull Long userId);
}
