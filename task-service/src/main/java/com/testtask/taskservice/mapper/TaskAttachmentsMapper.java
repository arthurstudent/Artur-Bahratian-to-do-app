package com.testtask.taskservice.mapper;

import com.testtask.taskservice.exceptions.custom.DataMappingException;
import com.testtask.taskservice.model.dto.data.AttachmentDto;
import com.testtask.taskservice.model.entity.TaskAttachment;
import com.testtask.taskservice.model.entity.TaskEntity;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Log4j2
@Component
@NoArgsConstructor
public class TaskAttachmentsMapper {

    public TaskAttachment toAttachment(@NonNull MultipartFile file, @NonNull TaskEntity task) {
        try {
            String contentType = StringUtils.substringAfter(file.getContentType(), "/");
            return new TaskAttachment()
                    .setAttachment(file.getBytes())
                    .setAttachmentName(file.getOriginalFilename())
                    .setAttachmentType(contentType)
                    .setTask(task)
                    .setFullName(file.getName() + contentType);
        } catch (IOException e) {
            log.warn("Unable to create attachment for tack with id - {}", task.getId(), e);
            throw new DataMappingException(e.getMessage());
        }
    }

    public AttachmentDto toAttachmentDto(@NonNull TaskAttachment attachment) {
        AttachmentDto attachmentDto = new AttachmentDto();
        attachmentDto.setFileName(attachmentDto.getFileName());
        attachmentDto.setId(attachment.getId());
        attachmentDto.setCreatedAt(attachment.getCreatedAt());
        return attachmentDto;
    }
}
