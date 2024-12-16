package com.testtask.taskservice.dao;

import com.testtask.taskservice.model.entity.TaskAttachment;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class TaskAttachmentDaoServiceImpl implements TaskAttachmentDaoService {

    private final AttachmentsRepository attachmentsRepository;

    @Transactional
    @Override
    public void delete(@NonNull Long attachmentId) {
        attachmentsRepository.deleteById(attachmentId);
    }

    @Override
    public List<TaskAttachment> findAllByTaskId(@NonNull Long taskId) {
        return attachmentsRepository.findByTaskId(taskId);
    }

    @Transactional
    @Override
    public void save(@NonNull TaskAttachment taskAttachment) {
        attachmentsRepository.save(taskAttachment);
        log.info("Task attachment saved with id {}", taskAttachment.getId());
    }

    @Transactional
    @Override
    public void save(@NonNull List<TaskAttachment> taskAttachments) {
        attachmentsRepository.saveAll(taskAttachments);
        log.info("Task attachment - {} saved", taskAttachments.size());
    }

    @Transactional
    @Override
    public void deleteAllByTaskId(@NonNull Long taskId) {
        attachmentsRepository.deleteByTaskId(taskId);
        log.info("Attachment deleted by taskId: {}", taskId);
    }
}
