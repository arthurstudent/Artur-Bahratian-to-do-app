package com.testtask.taskservice.model.dto;

import com.testtask.taskservice.model.entity.TaskStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
@NoArgsConstructor
public class UpdateTaskDto {

    private String taskName;

    private String taskDescription;

    private TaskStatus taskStatus;

    private Long taskId;

    private Long userId;
}
