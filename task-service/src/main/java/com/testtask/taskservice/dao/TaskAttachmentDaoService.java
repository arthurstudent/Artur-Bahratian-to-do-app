package com.testtask.taskservice.dao;

import com.testtask.taskservice.model.entity.TaskAttachment;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TaskAttachmentDaoService {

    @Transactional
    void delete(Long taskId);

    List<TaskAttachment> findAllByTaskId(Long taskId);

    @Transactional
    void save(TaskAttachment taskAttachment);

    @Transactional
    void save(List<TaskAttachment> taskAttachments);

    @Transactional
    void deleteAllByTaskId(Long taskId);
}
