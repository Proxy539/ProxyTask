package com.proxy.task.util;

import com.proxy.task.dto.TaskDto;
import com.proxy.task.entity.Task;
import com.proxy.task.entity.TaskStatus;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class TestUtil {

    public static final String TASK_NOT_FOUND_EXCEPTION_MESSAGE = "Task with id %d not found";

    public static final Long TASK_ID = 1L;
    public static final String TASK_TITLE = "taskTitle";
    public static final String TASK_DESCRIPTION = "taskDescription";
    public static final TaskStatus TASK_STATUS = TaskStatus.TODO;
    public static final LocalDate TASK_DUE_DATE = LocalDate.now();

    public static Task buildTask() {
        return buildTask(TASK_ID, TASK_TITLE, TASK_DESCRIPTION, TASK_STATUS, TASK_DUE_DATE );
    }

    public static Task buildTask(Long id, String title, String description, TaskStatus status, LocalDate dueDate) {
        return Task.builder()
                .id(id)
                .title(title)
                .description(description)
                .status(status)
                .dueDate(dueDate)
                .build();
    }

    public static TaskDto buildTaskDto() {
        return buildTaskDto(TASK_ID, TASK_TITLE, TASK_DESCRIPTION, TASK_STATUS, TASK_DUE_DATE);
    }

    public static TaskDto buildTaskDto(Long id, String title, String description, TaskStatus status, LocalDate dueDate) {
        return TaskDto.builder()
                .id(id)
                .title(title)
                .description(description)
                .status(status)
                .dueDate(dueDate)
                .build();
    }

    public static Task buildEmptyTask() {
        return new Task();
    }

    public static TaskDto buildEmptyTaskDto() {
        return new TaskDto();
    }

    public static Map<String, String> getErrorsMap() {
        var errors = new LinkedHashMap<String, String>();
        errors.put("dueDate", "Task due date cannot be empty");
        errors.put("description", "Task description cannot be empty");
        errors.put("title", "Task title cannot be empty");
        errors.put("status", "Task status cannot be empty");

        return errors;
    }
}
