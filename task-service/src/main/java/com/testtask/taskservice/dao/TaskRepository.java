package com.testtask.taskservice.dao;

import com.testtask.taskservice.model.entity.TaskEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface TaskRepository extends JpaRepository<TaskEntity, Long>, JpaSpecificationExecutor<TaskEntity> {

    TaskEntity findByUserIdAndId(Long userId, Long taskId);

    boolean existsByIdAndUserId(@NonNull Long taskId, @NonNull Long userId);

    void deleteById(@NonNull Long taskId);
}
