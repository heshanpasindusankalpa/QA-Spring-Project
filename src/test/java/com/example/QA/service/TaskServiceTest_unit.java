package com.example.QA.service;

import com.example.QA.model.Task;
import com.example.QA.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest_unit {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task sampleTask;

    @BeforeEach
    void setUp() {
        sampleTask = new Task();
        sampleTask.setId("1");
        sampleTask.setTitle("Test Task");
        sampleTask.setDescription("Test Description");
        sampleTask.setCompleted(false);
    }

    @Test
    void shouldCreateTask() {
        // Given
        when(taskRepository.save(any(Task.class))).thenReturn(sampleTask);

        // When
        Task createdTask = taskService.createTask("Test Task", "Test Description");

        // Then
        assertNotNull(createdTask);
        assertEquals("Test Task", createdTask.getTitle());
        assertEquals("Test Description", createdTask.getDescription());
        assertFalse(createdTask.isCompleted());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void shouldThrowExceptionForInvalidTask() {
        // Given & When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            taskService.createTask("", "Description");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            taskService.createTask(null, "Description");
        });
    }

    @Test
    void shouldGetAllTasks() {
        // Given
        List<Task> tasks = Arrays.asList(sampleTask);
        when(taskRepository.findAll()).thenReturn(tasks);

        // When
        List<Task> result = taskService.getAllTasks();

        // Then
        assertEquals(1, result.size());
        assertEquals(sampleTask, result.get(0));
        verify(taskRepository).findAll();
    }

    @Test
    void shouldCompleteTask() {
        // Given
        when(taskRepository.findById("1")).thenReturn(Optional.of(sampleTask));
        when(taskRepository.save(any(Task.class))).thenReturn(sampleTask);

        // When
        Task completedTask = taskService.completeTask("1");

        // Then
        assertTrue(completedTask.isCompleted());
        verify(taskRepository).findById("1");
        verify(taskRepository).save(sampleTask);
    }
}