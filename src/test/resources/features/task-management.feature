@Cucumber
Feature: Task Management
  As a user
  I want to manage my tasks
  So that I can track my work efficiently

  Background:
    Given the task management system is running

  Scenario: Successfully add a new task
    Given I am a registered user
    When I create a task with title "Complete BDD tutorial" and description "Learn Cucumber with Spring Boot"
    Then the task should be created successfully
    And the task should have title "Complete BDD tutorial"
    And the task should have description "Learn Cucumber with Spring Boot"
    And the task should be marked as not completed

  Scenario: Validate task title is required
    Given I am a registered user
    When I try to create a task with empty title ""
    Then I should get an error message "Task title cannot be null or empty"
    And the task should not be created

  Scenario: Complete a task
    Given I am a registered user
    And I have a pending task with title "Review code"
    When I mark the task as completed
    Then the task should be marked as completed
    And the task should appear in completed tasks list

  Scenario: View all tasks
    Given I am a registered user
    And I have created the following tasks:
      | title           | description              | completed |
      | Buy groceries   | Milk, bread, eggs       | false     |
      | Review PR       | Code review for feature  | true      |
      | Write tests     | Unit and integration     | false     |
    When I request all tasks
    Then I should see 3 tasks
    And I should see 2 pending tasks
    And I should see 1 completed task

  Scenario Outline: Validate task title length
    Given I am a registered user
    When I try to create a task with title "<title>"
    Then I should get result "<result>"

    Examples:
      | title                                                                                               | result  |
      | Valid task                                                                                         | success |
      | This is a very long title that exceeds one hundred characters and should fail validation testing | error   |
