package com.testtask.taskservice.dao;

import com.testtask.taskservice.model.entity.TaskAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface AttachmentsRepository extends JpaRepository<TaskAttachment, Long> {
    List<TaskAttachment> findByTaskId(Long taskId);

    void deleteByTaskId(Long taskId);
}
