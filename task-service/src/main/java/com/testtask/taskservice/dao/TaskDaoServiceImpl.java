package com.testtask.taskservice.dao;

import com.testtask.taskservice.model.dto.TaskFilterDto;
import com.testtask.taskservice.model.entity.TaskEntity;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
class TaskDaoServiceImpl implements TaskDaoService {

    private final TaskRepository taskRepository;

    @Override
    public Page<TaskEntity> getAllTasksByUserId(@NonNull TaskFilterDto taskFilterDto) {
        return taskRepository.findAll(TaskSpecification.filterBy(taskFilterDto), taskFilterDto.toPageable());
    }

    @Override
    public TaskEntity getUserTaskById(@NonNull Long id, @NonNull Long userId) {
        return Optional.ofNullable(taskRepository.findByUserIdAndId(userId, id))
                .orElseThrow(() -> {
                    log.warn("Task with id {} not found", id);
                    return new EntityNotFoundException("Task with is not found");
                });
    }

    @Transactional
    @Override
    public void save(TaskEntity task) {
        taskRepository.save(task);
    }

    @Transactional
    @Override
    public void deleteUserTask(@NonNull Long id) {
        taskRepository.deleteById(id);
        log.info("Task with id - {} deleted", id);
    }

    @Override
    public boolean existsUserTaskById(@NonNull Long id, @NonNull Long userId) {
        return taskRepository.existsByIdAndUserId(id, userId);
    }
}
