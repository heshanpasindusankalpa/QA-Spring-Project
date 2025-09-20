package com.example.QA.repository;

import com.example.QA.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface TaskRepository extends MongoRepository<Task, String> {
    List<Task> findByCompleted(boolean completed);
    List<Task> findByTitleContainingIgnoreCase(String title);
}