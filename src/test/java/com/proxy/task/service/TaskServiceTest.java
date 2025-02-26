package com.proxy.task.service;

import com.proxy.task.dto.TaskDto;
import com.proxy.task.entity.Task;
import com.proxy.task.exception.TaskNotFoundException;
import com.proxy.task.mapper.TaskMapper;
import com.proxy.task.repository.TaskRepository;
import com.proxy.task.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.proxy.task.util.TestUtil.TASK_ID;
import static com.proxy.task.util.TestUtil.buildTaskDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    public void givenNoTaskExistWhenFindAllTaskThenReturnEmptyList() {
        var emptyTasks = List.<Task>of();
        var emptyTasksDto = List.<TaskDto>of();
        when(taskRepository.findAll()).thenReturn(emptyTasks);
        when(taskMapper.toDtoList(emptyTasks)).thenReturn(emptyTasksDto);

        var result = taskService.findAll();

        assertThat(result).isEmpty();

        verify(taskRepository).findAll();
        verify(taskMapper).toDtoList(emptyTasks);
    }

    @Test
    public void givenTaskExistWhenFindAllTasksThenReturnTasksList() {
        var task = TestUtil.buildTask();
        var taskDto = TestUtil.buildTaskDto();
        var tasks = List.of(task);
        var tasksDto = List.of(taskDto);

        when(taskRepository.findAll()).thenReturn(tasks);
        when(taskMapper.toDtoList(tasks)).thenReturn(tasksDto);

        var result = taskService.findAll();

        assertThat(result).isEqualTo(tasksDto);

        verify(taskRepository).findAll();
        verify(taskMapper).toDtoList(tasks);
    }

    @Test
    public void givenTaskExistWhenFindByIdThenReturnTask() {
        var task = TestUtil.buildTask();
        var taskDto = TestUtil.buildTaskDto();

        when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(task));
        when(taskMapper.toDto(task)).thenReturn(taskDto);

        var result = taskService.findById(TASK_ID);

        assertThat(result).isEqualTo(taskDto);

        verify(taskRepository).findById(TASK_ID);
        verify(taskMapper).toDto(task);
    }

    @Test
    public void givenTaskNotExistsWhenFindByIdThenThrowException() {

        when(taskRepository.findById(TASK_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.findById(TASK_ID))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessage(TestUtil.TASK_NOT_FOUND_EXCEPTION_MESSAGE.formatted(TASK_ID));

        verify(taskRepository).findById(TASK_ID);
    }

    @Test
    public void whenSaveTaskThenReturnResult() {
        var task = TestUtil.buildTask();
        var taskDto = TestUtil.buildTaskDto();

        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toEntity(taskDto)).thenReturn(task);
        when(taskMapper.toDto(task)).thenReturn(taskDto);

        var result = taskService.save(taskDto);

        assertThat(result).isEqualTo(taskDto);

        verify(taskRepository).save(task);
        verify(taskMapper).toDto(task);
        verify(taskMapper).toEntity(taskDto);
    }

    @Test
    public void givenTaskExistsWhenUpdateTaskThenReturnResult() {
        var task = TestUtil.buildTask();
        var taskDto = TestUtil.buildTaskDto();

        when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(task));
        when(taskMapper.toDto(task)).thenReturn(taskDto);
        when(taskRepository.save(task)).thenReturn(task);

        var result = taskService.update(TASK_ID, taskDto);

        assertThat(result).isEqualTo(taskDto);

        verify(taskRepository).findById(TASK_ID);
        verify(taskMapper).toDto(task);
        verify(taskRepository).save(task);
    }

    @Test
    public void givenTaskNotExistsWhenUpdateTaskThenThrowException() {
        var taskDto = buildTaskDto();

        when(taskRepository.findById(TASK_ID)).thenReturn(Optional.empty());


        assertThatThrownBy(() -> taskService.update(TASK_ID, taskDto))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessage(TestUtil.TASK_NOT_FOUND_EXCEPTION_MESSAGE.formatted(TASK_ID));

        verify(taskRepository).findById(TASK_ID);
    }

    @Test
    public void givenTaskExistsWhenDeleteTaskThenDeleteTask() {

        taskService.delete(TASK_ID);

        verify(taskRepository).deleteById(TASK_ID);
    }
}
