package com.example.QA.bdd;

import com.example.QA.model.Task;
import com.example.QA.service.TaskService;
import io.cucumber.java.en.*;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@CucumberContextConfiguration
public class TaskManagementSteps {

    @Autowired
    private TaskService taskService;

    private Task currentTask;
    private List<Task> allTasks;
    private String errorMessage;

    @Given("the task management system is running")
    public void taskManagementSystemIsRunning() {
        assertNotNull(taskService);
    }

    @Given("I am a registered user")
    public void iAmARegisteredUser() {
        // Assume registered user
    }

    @When("I create a task with title {string} and description {string}")
    public void iCreateATask(String title, String description) {
        try {
            currentTask = taskService.createTask(title, description);
        } catch (IllegalArgumentException e) {
            errorMessage = e.getMessage();
        }
    }

    @Then("the task should be created successfully")
    public void taskShouldBeCreatedSuccessfully() {
        assertNotNull(currentTask);
    }

    @Then("the task should have title {string}")
    public void taskShouldHaveTitle(String title) {
        assertEquals(title, currentTask.getTitle());
    }

    @Then("the task should have description {string}")
    public void taskShouldHaveDescription(String description) {
        assertEquals(description, currentTask.getDescription());
    }

    @Then("the task should be marked as not completed")
    public void taskShouldBeMarkedAsNotCompleted() {
        assertFalse(currentTask.isCompleted());
    }

    @When("I try to create a task with empty title {string}")
    public void iTryToCreateTaskWithEmptyTitle(String title) {
        try {
            currentTask = taskService.createTask(title, "Some description");
        } catch (IllegalArgumentException e) {
            errorMessage = e.getMessage();
        }
    }

    @Then("I should get an error message {string}")
    public void iShouldGetErrorMessage(String expectedMessage) {
        assertEquals(expectedMessage, errorMessage);
    }

    @Then("the task should not be created")
    public void taskShouldNotBeCreated() {
        assertNull(currentTask);
    }

    @Given("I have a pending task with title {string}")
    public void iHaveAPendingTask(String title) {
        currentTask = taskService.createTask(title, "Sample description");
    }

    @When("I mark the task as completed")
    public void iMarkTaskAsCompleted() {
        currentTask = taskService.completeTask(currentTask.getId());
    }

    @Then("the task should be marked as completed")
    public void taskShouldBeCompleted() {
        assertTrue(currentTask.isCompleted());
    }

    @Then("the task should appear in completed tasks list")
    public void taskShouldAppearInCompletedList() {
        List<Task> completedTasks = taskService.getCompletedTasks();
        assertTrue(completedTasks.contains(currentTask));
    }

    @Given("I have created the following tasks:")
    public void iHaveCreatedTasks(List<Task> tasks) {
        tasks.forEach(task -> taskService.createTask(task.getTitle(), task.getDescription()));
    }

    @When("I request all tasks")
    public void iRequestAllTasks() {
        allTasks = taskService.getAllTasks();
    }

    @Then("I should see {int} tasks")
    public void iShouldSeeTasks(int total) {
        assertEquals(total, allTasks.size());
    }

    @Then("I should see {int} pending tasks")
    public void iShouldSeePendingTasks(int pending) {
        long count = allTasks.stream().filter(t -> !t.isCompleted()).count();
        assertEquals(pending, count);
    }

    @Then("I should see {int} completed task")
    public void iShouldSeeCompletedTasks(int completed) {
        long count = allTasks.stream().filter(Task::isCompleted).count();
        assertEquals(completed, count);
    }

    @When("I try to create a task with title {string}")
    public void iTryToCreateTaskWithTitle(String title) {
        try {
            currentTask = taskService.createTask(title, "Description");
            errorMessage = currentTask != null ? "success" : "error";
        } catch (IllegalArgumentException e) {
            errorMessage = "error";
        }
    }

    @Then("I should get result {string}")
    public void iShouldGetResult(String result) {
        assertEquals(result, errorMessage);
    }
}