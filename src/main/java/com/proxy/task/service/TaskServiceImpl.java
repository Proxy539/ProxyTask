package com.proxy.task.service;

import com.proxy.task.dto.TaskDto;
import com.proxy.task.entity.Task;
import com.proxy.task.entity.TaskStatus;
import com.proxy.task.exception.TaskNotFoundException;
import com.proxy.task.mapper.TaskMapper;
import com.proxy.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private static final String TASK_NOT_FOUND_EXCEPTION_MESSAGE = "Task with id %d not found";

    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;

    @Override
    public List<TaskDto> findAll() {
        log.info("Looking for all tasks");

        var tasks = taskRepository.findAll();

        log.info("{} tasks found", tasks.size());

        return taskMapper.toDtoList(tasks);
    }

    @Override
    public Page<TaskDto> findTaskPage(String title, String description, TaskStatus taskStatus, LocalDate dueDate, int page, int size, Sort sort) {
        log.info("Looking for tasks page. Page = {}, size = {}, sort = {}", page, size, sort);

        var pageRequest = PageRequest.of(page, size, sort);

        var filterSpecification = TaskSpecification.filterBy(title, description, taskStatus, dueDate);

        var pageResponse = taskRepository.findAll(filterSpecification, pageRequest);

        log.info("Found {} tasks page", pageRequest.getPageSize());

        return pageResponse.map(taskMapper::toDto);
    }

    @Override
    public TaskDto findById(Long taskId) {
        log.info("Looking for task with id {}", taskId);

        var optionalTask = taskRepository.findById(taskId);
        var task = optionalTask.orElseThrow(() -> new TaskNotFoundException(TASK_NOT_FOUND_EXCEPTION_MESSAGE.formatted(taskId)));

        log.info("Task with id {} found", taskId);

        return taskMapper.toDto(task);
    }

    @Override
    public TaskDto save(TaskDto taskDto) {
        log.info("Saving new task");

        var task = taskMapper.toEntity(taskDto);
        var savedTask = taskRepository.save(task);

        log.info("New task with id {} saved", savedTask.getId());

        return taskMapper.toDto(savedTask);
    }

    @Override
    public TaskDto update(Long taskId, TaskDto taskDto) {
        log.info("Updating task with id {}", taskId);

        var optionalTask = taskRepository.findById(taskId);
        var task = optionalTask.orElseThrow(() -> new TaskNotFoundException(TASK_NOT_FOUND_EXCEPTION_MESSAGE.formatted(taskId)));

        taskMapper.updateTask(taskDto, task);

        var updatedTask = taskRepository.save(task);

        log.info("Task with {} updated", taskId);

        return taskMapper.toDto(updatedTask);
    }

    @Override
    public void delete(Long taskId) {
        log.info("Deleting task with id {}", taskId);

        taskRepository.deleteById(taskId);

        log.info("Task with id {} deleted", taskId);
    }
}
