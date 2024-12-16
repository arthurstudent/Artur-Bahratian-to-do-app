package com.testtask.taskservice.api;

import com.testtask.taskservice.model.dto.AddAttachmentDto;
import com.testtask.taskservice.model.dto.UpdateTaskDto;
import com.testtask.taskservice.model.dto.data.TaskDto;
import com.testtask.taskservice.model.dto.data.TaskDtoSingle;
import com.testtask.taskservice.model.dto.request.CreateTaskDto;
import com.testtask.taskservice.model.dto.request.GetUserTasksRequestDto;
import com.testtask.taskservice.model.dto.request.UpdateTaskRequestDto;
import com.testtask.taskservice.model.dto.response.ApiResponse;
import com.testtask.taskservice.model.security.CustomUserPrincipal;
import com.testtask.taskservice.service.TaskManagerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RequestMapping("/api/v1/tasks")
@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TaskManagerService taskManagerService;

    @GetMapping("/get-task/{taskId}")
    public ResponseEntity<?> getTask(@PathVariable Long taskId, Authentication authentication) {
        CustomUserPrincipal customUserPrincipal = (CustomUserPrincipal) authentication.getPrincipal();
        TaskDto taskById = taskManagerService.getUserTaskById(taskId, customUserPrincipal.getUserId());
        ApiResponse<TaskDto> taskDtoApiResponse = ApiResponse.successResponse(taskById);
        return ResponseEntity.ok().body(taskDtoApiResponse);
    }

    @PostMapping("get-all-user-tasks")
    public ResponseEntity<?> getAllTasks(@RequestBody @Valid GetUserTasksRequestDto getUserTasksRequestDto, Authentication authentication) {
        CustomUserPrincipal customUserPrincipal = (CustomUserPrincipal) authentication.getPrincipal();
        List<TaskDtoSingle> allUserTasks = taskManagerService.getAllUserTasks(customUserPrincipal.getUserId(), getUserTasksRequestDto);
        ApiResponse<List<TaskDtoSingle>> listApiResponse = ApiResponse.successResponse(allUserTasks);
        return ResponseEntity.ok().body(listApiResponse);
    }

    @GetMapping("/file/{fileName}")
    public ResponseEntity<Resource> getFileByName(@PathVariable String fileName) {
        Resource resource = taskManagerService.getFileByName(fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filaName=\"" + resource.getFilename() + "\"").body(resource);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestParam @NotBlank @Size(min = 3, max = 50) String taskName,
                                        @RequestParam @NotBlank @Size(min = 3, max = 300) String description,
                                        @RequestParam(required = false) MultipartFile[] files,
                                        Authentication authentication) {
        CustomUserPrincipal customUserPrincipal = (CustomUserPrincipal) authentication.getPrincipal();

        CreateTaskDto createTaskDto = new CreateTaskDto()
                .setTaskName(taskName)
                .setDescription(description)
                .setUserId(customUserPrincipal.getUserId());

        if (Objects.nonNull(files) && files.length > 0) {
            createTaskDto.setFiles(Arrays.asList(files));
        }

        taskManagerService.createTask(createTaskDto);

        ApiResponse<?> apiResponse = ApiResponse.successResponse();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PostMapping("add-file/{taskId}")
    public ResponseEntity<?> addAttachments(@PathVariable Long taskId, @RequestParam @NotEmpty MultipartFile[] files, Authentication authentication) {
        CustomUserPrincipal customUserPrincipal = (CustomUserPrincipal) authentication.getPrincipal();

        var attachmentDto = new AddAttachmentDto()
                .setTaskId(taskId)
                .setUserId(customUserPrincipal.getUserId())
                .setFiles(Arrays.asList(files));

        taskManagerService.addAttachment(attachmentDto);
        ApiResponse<?> apiResponse = ApiResponse.successResponse();
        return ResponseEntity.ok().body(apiResponse);
    }

    @PutMapping("/update/{taskId}")
    public ResponseEntity<?> updateTask(@PathVariable Long taskId, @RequestBody UpdateTaskRequestDto updateTaskRequestDto, Authentication authentication) {
        CustomUserPrincipal customUserPrincipal = (CustomUserPrincipal) authentication.getPrincipal();

        var updateTaskDto = new UpdateTaskDto()
                .setTaskId(taskId)
                .setTaskName(updateTaskRequestDto.getTaskName())
                .setTaskDescription(updateTaskRequestDto.getTaskDescription())
                .setUserId(customUserPrincipal.getUserId())
                .setTaskStatus(updateTaskRequestDto.getTaskStatus());

        taskManagerService.updateTask(updateTaskDto);
        ApiResponse<?> apiResponse = ApiResponse.successResponse();
        return ResponseEntity.ok().body(apiResponse);
    }

    @DeleteMapping("{taskId}/delete-file/{fileId}")
    public ResponseEntity<?> deleteFile(@PathVariable Long taskId, @PathVariable Long fileId, Authentication authentication) {
        CustomUserPrincipal customUserPrincipal = (CustomUserPrincipal) authentication.getPrincipal();
        taskManagerService.deleteAttachment(customUserPrincipal.getUserId(), taskId, fileId);
        ApiResponse<?> apiResponse = ApiResponse.successResponse();
        return ResponseEntity.ok().body(apiResponse);
    }

    @DeleteMapping("delete-task/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable Long taskId, Authentication authentication) {
        CustomUserPrincipal customUserPrincipal = (CustomUserPrincipal) authentication.getPrincipal();
        taskManagerService.deleteTask(taskId, customUserPrincipal.getUserId());
        ApiResponse<?> apiResponse = ApiResponse.successResponse();
        return ResponseEntity.ok().body(apiResponse);
    }
}
