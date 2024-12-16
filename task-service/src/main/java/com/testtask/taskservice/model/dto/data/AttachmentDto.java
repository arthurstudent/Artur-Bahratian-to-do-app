package com.testtask.taskservice.model.dto.data;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AttachmentDto {

    private String fileName;

    private Long id;

    private LocalDateTime createdAt;
}
