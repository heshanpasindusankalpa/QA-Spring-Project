package com.example.QA.service;

import com.example.QA.model.Task;
import com.example.QA.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public Task createTask(String title, String description) {
        // Add validation
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Task title cannot be null or empty");
        }

        if (title.length() > 100) { // Add length validation
            throw new IllegalArgumentException("Task title cannot exceed 100 characters");
        }

        // Rest of your creation logic
        Task task = new Task(title, description);
        return taskRepository.save(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task completeTask(String taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setCompleted(true);
        return taskRepository.save(task);
    }

    public List<Task> getCompletedTasks() {
        return taskRepository.findByCompleted(true);
    }

    public List<Task> getPendingTasks() {
        return taskRepository.findByCompleted(false);
    }
}