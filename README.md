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