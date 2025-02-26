package com.proxy.task.dto;

import com.proxy.task.entity.TaskStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskDto {
    private Long id;
    @NotNull(message = "Task title cannot be empty")
    private String title;
    @NotNull(message = "Task description cannot be empty")
    private String description;
    @NotNull(message = "Task status cannot be empty")
    private TaskStatus status;
    @NotNull(message = "Task due date cannot be empty")
    private LocalDate dueDate;
}
