package com.proxy.task.controller;

import com.proxy.task.dto.TaskDto;
import com.proxy.task.entity.TaskStatus;
import com.proxy.task.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public List<TaskDto> findAllTasks() {
        return taskService.findAll();
    }

    @GetMapping("/page")
    public Page<TaskDto> findTaskPage(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) LocalDate dueDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Sort sort) {
        return taskService.findTaskPage(title, description, status, dueDate, page, size, sort);
    }

    @GetMapping("/{taskId}")
    public TaskDto findById(@PathVariable Long taskId) {
        return taskService.findById(taskId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDto save(@RequestBody @Valid TaskDto taskDto) {
        return taskService.save(taskDto);
    }

    @PutMapping("/{taskId}")
    public TaskDto update(@PathVariable Long taskId, @RequestBody @Valid TaskDto taskDto) {
        return taskService.update(taskId, taskDto);
    }

    @DeleteMapping("/{taskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long taskId) {
        taskService.delete(taskId);
    }
}
