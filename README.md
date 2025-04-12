# 🎓 QuizLand - Java Swing Quiz Application

**QuizLand** is a desktop quiz management application built using Java Swing and SQLite. This app allows users to create, manage, and take quizzes in a visually friendly and user-friendly environment. It's ideal for educators, students, or anyone who wants to manage quizzes and tasks.

---

## ✨ Features

- ✅ **Create New Quizzes** with a title and manage your own collection.
- 📝 **Start a Quiz** and go through a set of tasks/questions one by one.
- ✏️ **Edit Existing Quizzes** – modify quiz title, tasks, answers, and scores.
- ❌ **Delete Quizzes** that you no longer need (only by the author).
- 📦 **Import Quizzes from CSV** – quickly bulk load tasks for a quiz.
- 📤 **Export Quiz to CSV** – save a quiz's tasks in CSV format for backup or sharing.
- 📜 **Session Handling** – edit/remove only your own quizzes (via session variable).
- 🎨 **Clean UI** with scrollable panels and neatly stacked quiz cards.
- 💾 **SQLite Database** integration to persist quizzes and tasks.

---

## 🛠️ How to Run

### 1. **Requirements**

- Java Development Kit (JDK) 8 or higher
- SQLite JDBC Driver (`sqlite-jdbc-[version].jar`)

### 2. **Compile the Application**

```bash
javac -cp ".:libs/sqlite-jdbc-3.49.1.0.jar" Main.java LaunchPage.java MenuPage.java Utils.java User.java QuizPage.java
```

### 3. **Run the Application**

``` bash
java -cp ".:libs/sqlite-jdbc-3.49.1.0.jar" Main.java
```

### 📁 Project Structure

``` bash
QuizApp/
├── static/
│   └── Home.png               # Icon used in the app
├── quiz.db                    # SQLite Database file
├── Utils.java                 # Helper methods & session management
├── Task.java                  # Task model
├── Quiz.java                  # Quiz model (CRUD)
├── QuizPage.java              # Page to take the quiz
├── QuizEditPage.java          # Page to edit a quiz
├── MenuPage.java              # Main menu screen
├── Main.java                  # Entry point of the app
└── README.md                  # This file

```

### 📥 CSV Format for Importing

To import a CSV for a quiz, make sure your file follows this structure:


``` arduino
title,description,answer,points
"Task 1","Description of task","Answer","10"
"Task 2","Another description","Another answer","5"
```

👤 Author Management

Each quiz is linked to an author based on the session. Only the author can:

    Edit the quiz

    Remove the quiz

Session can be set via the Utils.session variable in the code.

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