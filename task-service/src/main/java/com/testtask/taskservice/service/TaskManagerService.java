package com.testtask.taskservice.service;

import com.testtask.taskservice.config.configurer.FileManagerConfigurer;
import com.testtask.taskservice.dao.TaskAttachmentDaoService;
import com.testtask.taskservice.dao.TaskDaoService;
import com.testtask.taskservice.exceptions.custom.FileProcessingException;
import com.testtask.taskservice.mapper.TaskAttachmentsMapper;
import com.testtask.taskservice.mapper.TaskMapper;
import com.testtask.taskservice.model.dto.AddAttachmentDto;
import com.testtask.taskservice.model.dto.TaskFilterDto;
import com.testtask.taskservice.model.dto.UpdateTaskDto;
import com.testtask.taskservice.model.dto.data.AttachmentDto;
import com.testtask.taskservice.model.dto.data.TaskDto;
import com.testtask.taskservice.model.dto.data.TaskDtoSingle;
import com.testtask.taskservice.model.dto.request.CreateTaskDto;
import com.testtask.taskservice.model.dto.request.GetUserTasksRequestDto;
import com.testtask.taskservice.model.entity.TaskAttachment;
import com.testtask.taskservice.model.entity.TaskEntity;
import com.testtask.taskservice.model.entity.TaskStatus;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class TaskManagerService {

    private static final long MAX_SIZE = 50 * 1024 * 1024;

    private final TaskAttachmentDaoService taskAttachmentDaoService;

    private final TaskDaoService taskDaoService;

    private final TaskMapper taskMapper;

    private final FileManagerService fileManagerService;

    private final TaskAttachmentsMapper taskAttachmentsMapper;

    private final FileManagerConfigurer fileManagerConfigurer;

    @Transactional
    public void createTask(@NonNull CreateTaskDto createTaskDto) {
        TaskEntity task = new TaskEntity();
        task.setTitle(createTaskDto.getTaskName());
        task.setDescription(createTaskDto.getDescription());
        task.setTaskStatus(TaskStatus.NOT_STARTED);
        task.setUserId(createTaskDto.getUserId());

        List<TaskAttachment> taskAttachmentList = createTaskDto.getFiles().stream()
                .map(file -> {
                    checkFileSize(file);
                    return taskAttachmentsMapper.toAttachment(file, task);
                })
                .toList();

        taskDaoService.save(task);
        taskAttachmentDaoService.save(taskAttachmentList);
    }

    public void updateTask(@NonNull UpdateTaskDto updateTaskDto) {
        Long taskId = updateTaskDto.getTaskId();
        Long userId = updateTaskDto.getUserId();

        TaskEntity task = Optional.ofNullable(taskDaoService.getUserTaskById(taskId, userId)).orElseThrow(() -> {
            log.warn("Task with id {} not found to update", taskId);
            return new EntityNotFoundException("Task with is not found");
        });

        if (updateTaskDto.getTaskName() != null) {
            task.setTitle(updateTaskDto.getTaskName());
        }
        if (updateTaskDto.getTaskDescription() != null) {
            task.setDescription(updateTaskDto.getTaskDescription());
        }
        if (updateTaskDto.getTaskStatus() != null) {
            task.setTaskStatus(updateTaskDto.getTaskStatus());
        }

        taskDaoService.save(task);
    }

    public List<TaskDtoSingle> getAllUserTasks(@NonNull Long userId, @NonNull GetUserTasksRequestDto getUserTasksRequestDto) {
        TaskFilterDto taskFilterDto = new TaskFilterDto()
                .setTaskStatus(getUserTasksRequestDto.getTaskStatus())
                .setUserId(userId)
                .setPage(getUserTasksRequestDto.getPage())
                .setSize(getUserTasksRequestDto.getPageSize())
                .setTitle(getUserTasksRequestDto.getTaskName());

        List<TaskDtoSingle> allTasks = taskDaoService.getAllTasksByUserId(taskFilterDto).stream()
                .map(taskMapper::toTaskDtoSingle)
                .toList();

        log.info("Found - {} tasks for user with id - {}", allTasks.size(), userId);
        return allTasks;
    }

    public TaskDto getUserTaskById(@NonNull Long taskId, @NonNull Long userId) {
        TaskEntity taskById = Optional.ofNullable(taskDaoService.getUserTaskById(taskId, userId)).orElseThrow(() -> {
            log.warn("Task with id {} not found for user with id - {}", taskId, userId);
            return new EntityNotFoundException("Task with is not found");
        });

        List<AttachmentDto> allByTaskId = taskAttachmentDaoService.findAllByTaskId(taskId)
                .stream()
                .map(taskAttachment -> {
                    String attachmentName = taskAttachment.getAttachmentName();
                    String fullPath = fileManagerConfigurer.getStoreFilePath() + attachmentName;
                    fileManagerService.writeToFile(fullPath, taskAttachment.getAttachment());

                    return taskAttachmentsMapper.toAttachmentDto(taskAttachment);
                }).toList();
        log.info("Found and save to temp dir - {} attachments for task with id - {}", allByTaskId.size(), taskId);

        return taskMapper.toTaskDto(taskById, allByTaskId);
    }

    public Resource getFileByName(String fileName) {
        return fileManagerService.getFileByName(fileName);
    }

    @Transactional
    public void deleteTask(@NonNull Long taskId, @NonNull Long userId) {
        boolean existsUserTaskById = taskDaoService.existsUserTaskById(taskId, userId);

        if (!existsUserTaskById) {
            log.warn("Task with id {} not found for user with id - {}", taskId, userId);
            throw new EntityNotFoundException("Task with is not found");
        }

        taskAttachmentDaoService.deleteAllByTaskId(taskId);
        taskDaoService.deleteUserTask(taskId);
        log.info("Deleted task with id - {} with all attachments", taskId);
    }

    public void deleteAttachment(@NonNull Long userId, @NonNull Long taskId, @NonNull Long attachmentId) {
        boolean existsUserTaskById = taskDaoService.existsUserTaskById(taskId, userId);

        if (!existsUserTaskById) {
            log.warn("Task with id {} not found for user with id - {}", taskId, userId);
            throw new EntityNotFoundException("Task with is not found");
        }

        taskAttachmentDaoService.delete(attachmentId);
        log.info("Deleted attachment with id - {}", attachmentId);
    }

    public void addAttachment(@NonNull AddAttachmentDto attachmentDto) {
        Long taskId = attachmentDto.getTaskId();

        TaskEntity task = Optional.ofNullable(taskDaoService.getUserTaskById(taskId, attachmentDto.getUserId())).orElseThrow(() -> {
            log.warn("Task with id {} not found to add attachments", taskId);
            return new EntityNotFoundException("Task with is not found");
        });

        List<MultipartFile> files = attachmentDto.getFiles();

        if (Objects.isNull(files) || files.isEmpty()) {
            throw new FileProcessingException("Files are not presented, please add files first");
        }

        List<TaskAttachment> attachmentList = files.stream()
                .map(file -> {
                    checkFileSize(file);
                    return taskAttachmentsMapper.toAttachment(file, task);

                })
                .toList();
        taskAttachmentDaoService.save(attachmentList);
        log.info("Added - {} attachments to task with id - {} with attachments", attachmentList.size(), taskId);
    }

    private void checkFileSize(@NonNull MultipartFile file) {
        if (file.getSize() > MAX_SIZE) {
            String originalFilename = file.getOriginalFilename();
            log.warn("Unable to process file with name - {}, to large, file size - {}", originalFilename, file.getSize());
            throw new FileProcessingException("File with name - %s is too large, max size - %sMB, please add files first"
                    .formatted(originalFilename, MAX_SIZE));
        }
    }
}
