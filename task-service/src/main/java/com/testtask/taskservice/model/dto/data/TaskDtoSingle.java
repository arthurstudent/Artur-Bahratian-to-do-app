package com.testtask.taskservice.model.dto.data;

import com.testtask.taskservice.model.entity.TaskStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TaskDtoSingle {

    private Long id;

    private String title;

    private String description;

    private TaskStatus taskStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
