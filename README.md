# Presentation 
link: https://docs.google.com/presentation/d/1MQ23QpsULkw2m0KhNPPAvXkGJTJtQAxHtCmsHK2IaD8/edit?usp=sharing
# 📚 Documentation – QuizLand Java Swing Application

---

## 📦 Overview

**QuizLand** is a Java desktop application built using **Swing** for the GUI and **SQLite** as a backend database. The main purpose is to allow users to create, edit, take, import/export, and delete quizzes.

This document covers:
- Main components
- Algorithms and data structures
- Core methods and modules
- Development challenges and resolutions

---

## 🧠 Data Structures & Models

### 1. `Quiz` class
Represents a quiz and holds metadata like ID, title, author, a list of tasks, and user answers.

- `List<Task> tasks`: Stores questions.
- `Map<Task, String> answers`: Maps user answers to each task.
- CRUD operations interact with SQLite database.

### 2. `Task` class
Represents an individual task/question in a quiz.

- Attributes: ID, title, description, correct answer, and point value.
- `boolean checkAnswer(String input)`: Validates the user's answer.

---

## 🧩 Algorithms & Logic

### 1. **Answer Checking Algorithm**
```java
int checkAnswers() {
    int correct = 0;
    for (Map.Entry<Task, String> entry : answers.entrySet()) {
        if (entry.getKey().checkAnswer(entry.getValue())) {
            correct++;
        }
    }
    return correct;
}
```
Simple linear traversal through answer map.

Time Complexity: O(n) where n = number of tasks.

2. Quiz Import/Export via CSV

    Export: Writes each task of a quiz into a .csv file line-by-line.

    Import: Reads tasks line-by-line, parses fields, and creates Task objects.

Error handling ensures files are valid and well-formatted.
📚 Key Classes and Modules
Quiz.java

    Manages the entire quiz entity.

    Handles database interactions for CRUD.

    Maintains task and answer list.

    Methods: add(), delete(), get(), getAll(), addTask(), checkAnswers().

Task.java

    Encapsulates the question details.

    Simple logic for answer checking.

MenuPage.java

    Main menu screen using JFrame and JPanel.

    Dynamically renders all quizzes.

    Provides UI actions: Start, Edit, Delete, Import, Export.

QuizEditPage.java

    GUI for modifying a quiz: edit title, add/remove/update tasks.

    Includes logic for exporting/importing from CSV.

QuizPage.java

    Used to display a quiz and allow users to answer questions.

    Navigates through tasks and shows result summary.

Utils.java

    Static helper methods and constants.

    Provides session handling and styling (fonts, frames).

    Manages CSV logic and SQLite connections.

🧱 SQLite Schema

    CREATE TABLE quiz (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        title TEXT,
        author TEXT
    );

    CREATE TABLE task (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        quiz_id INTEGER,
        title TEXT,
        description TEXT,
        answer TEXT,
        points INTEGER
    );

Foreign key quiz_id links each task to its quiz.
