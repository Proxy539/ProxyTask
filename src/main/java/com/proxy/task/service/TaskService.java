package com.proxy.task.service;

import com.proxy.task.dto.TaskDto;
import com.proxy.task.entity.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;

public interface TaskService {

    List<TaskDto> findAll();
    Page<TaskDto> findTaskPage(String title, String description, TaskStatus status, LocalDate dueDate, int page, int size, Sort sort);

    TaskDto findById(Long taskId);

    TaskDto save(TaskDto taskDto);

    TaskDto update(Long taskId, TaskDto taskDto);

    void delete(Long taskId);
}
