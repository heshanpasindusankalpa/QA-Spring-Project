package com.example.QA.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest_unitTest {

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
    }

    @Test
    void shouldCreateTaskWithValidData() {
        // Given
        String title = "Complete TDD tutorial";
        String description = "Implement TDD with Spring Boot";

        // When
        task.setTitle(title);
        task.setDescription(description);
        task.setCompleted(false);

        // Then
        assertEquals(title, task.getTitle());
        assertEquals(description, task.getDescription());
        assertFalse(task.isCompleted());
        assertNotNull(task.getId());
    }

    @Test
    void shouldValidateTaskTitle() {
        // Given & When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            task.setTitle("");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            task.setTitle(null);
        });
    }

    @Test
    void shouldValidateTaskTitleLength() {
        // Given
        String longTitle = "a".repeat(101);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            task.setTitle(longTitle);
        });
    }
}