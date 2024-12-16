package com.testtask.taskservice.model.dto;

import com.testtask.taskservice.model.entity.TaskStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class TaskFilterDto {
    private Long userId;
    private String title;
    private TaskStatus taskStatus;
    private Integer page = 0;
    private Integer size = 10;

    public Pageable toPageable() {
        return PageRequest.of(page, size);
    }
}