package com.proxy.task.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proxy.task.dto.TaskDto;
import com.proxy.task.exception.TaskNotFoundException;
import com.proxy.task.service.TaskService;
import com.proxy.task.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.MultiValueMap;

import javax.print.attribute.standard.Media;
import java.util.List;
import java.util.Map;

import static com.proxy.task.util.TestUtil.TASK_ID;
import static com.proxy.task.util.TestUtil.buildTaskDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.longThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TaskService taskService;

    @Test
    public void givenNoTasksWhenFindAllTasksThenReturnEmptyList() throws Exception {

        var emptyList = List.<TaskDto>of();
        when(taskService.findAll()).thenReturn(emptyList);

        var response = mockMvc.perform(get("/api/v1/tasks"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();


        assertThat(response).isEqualTo(objectMapper.writeValueAsString(emptyList));

        verify(taskService).findAll();
    }

    @Test
    public void givenTaskExistWhenFindAllTasksThenReturnTasksList() throws Exception {

        var tasks = List.of(buildTaskDto());

        when(taskService.findAll()).thenReturn(tasks);

        var response = mockMvc.perform(get("/api/v1/tasks"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(response).isEqualTo(objectMapper.writeValueAsString(tasks));

        verify(taskService).findAll();
    }

    @Test
    public void givenNoTasksWhenFindTaskPageThenReturnEmptyPage() throws Exception {

        var emptyPage = Page.<TaskDto>empty();

        var params = Map.of(
                "page", "0",
                "size", "10");

        var paramsMap = MultiValueMap.fromSingleValue(params);


        when(taskService.findTaskPage(null, null, null, null, 0, 10, Sort.unsorted()))
                .thenReturn(emptyPage);

        var response = mockMvc.perform(get("/api/v1/tasks/page")
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParams(paramsMap))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(response).isEqualTo(objectMapper.writeValueAsString(emptyPage));

        verify(taskService).findTaskPage(null, null, null, null, 0, 10, Sort.unsorted());
    }

    @Test
    public void givenTaskExistsWhenFindTaskPageThenReturnTasksPage() throws Exception {
        var taskDto = buildTaskDto();
        var tasksList = List.of(taskDto);
        var page = new PageImpl<>(tasksList);

        var paramsMap = MultiValueMap.fromSingleValue(Map.of(
                "page", "0",
                "size", "10"
        ));

        when(taskService.findTaskPage(null, null, null, null, 0, 10, Sort.unsorted()))
                .thenReturn(page);

        var response = mockMvc.perform(get("/api/v1/tasks/page")
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParams(paramsMap))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(response).isEqualTo(objectMapper.writeValueAsString(page));

        verify(taskService).findTaskPage(null, null, null, null, 0, 10, Sort.unsorted());
    }

    @Test
    public void givenNoTaskWhenFindTaskByIdThenNotFoundResponse() throws Exception {

        when(taskService.findById(TASK_ID))
                .thenThrow(TaskNotFoundException.class);

        var response = mockMvc.perform(get("/api/v1/tasks/{taskId}", TASK_ID))
                .andExpect(status().isNotFound());

        verify(taskService).findById(TASK_ID);
    }

    @Test
    public void givenTaskExistWhenFindTaskByIdThenReturnTask() throws Exception {

        var task = buildTaskDto();

        when(taskService.findById(TASK_ID)).thenReturn(task);


        var response = mockMvc.perform(get("/api/v1/tasks/{taskId}", TASK_ID))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();


        assertThat(response).isEqualTo(objectMapper.writeValueAsString(task));

        verify(taskService).findById(TASK_ID);
    }

    @Test
    public void whenSaveTaskThenReturnSavedTask() throws Exception {

        var taskDto = buildTaskDto();

        when(taskService.save(taskDto)).thenReturn(taskDto);

        var response = mockMvc.perform(post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(response).isEqualTo(objectMapper.writeValueAsString(taskDto));

        verify(taskService).save(taskDto);
    }

    @Test
    public void whenUpdateTaskThenReturnUpdatedTask() throws Exception {
        var taskDto = buildTaskDto();

        when(taskService.update(TASK_ID, taskDto)).thenReturn(taskDto);

        var response = mockMvc.perform(put("/api/v1/tasks/{taskId}", TASK_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(response).isEqualTo(objectMapper.writeValueAsString(taskDto));

        verify(taskService).update(TASK_ID, taskDto);
    }

    @Test
    public void givenNoTaskWhenUpdateTaskThenNotFoundResponse() throws Exception {
        var taskDto = buildTaskDto();

        when(taskService.update(TASK_ID, taskDto)).thenThrow(TaskNotFoundException.class);

        mockMvc.perform(put("/api/v1/tasks/{taskId}", TASK_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isNotFound());

        verify(taskService).update(TASK_ID, taskDto);
    }

    @Test
    public void whenDeleteTaskThenReturnNoContent() throws Exception {

        mockMvc.perform(delete("/api/v1/tasks/{taskId}", TASK_ID))
                .andExpect(status().isNoContent());

        verify(taskService).delete(TASK_ID);
    }
}
