import javax.swing.*;
// import java.awt.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.util.stream.Stream;



public class Quiz {
    private final List<Task> tasks = new ArrayList<>();
    private final Map<Task, String> answers = new HashMap<>();
    
    private final String title;
    private final String author;
    private final int id;

    public Quiz(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;

        String taskQuery = "SELECT * FROM task WHERE quiz_id = ?";

        try (Connection conn = DriverManager.getConnection(Utils.dbUrl)) {
            if (conn != null) {
                PreparedStatement taskStmt = conn.prepareStatement(taskQuery);
                taskStmt.setInt(1, id);
                ResultSet taskRs = taskStmt.executeQuery();
                while (taskRs.next()) {
                    int taskId = taskRs.getInt("id");
                    String taskTitle = taskRs.getString("title");
                    String description = taskRs.getString("description");
                    String answer = taskRs.getString("answer");
                    int points = taskRs.getInt("points");

                    addTask(new Task(taskId, taskTitle, description, answer, points));
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    static Quiz add(String title, String author){
        try (Connection conn = DriverManager.getConnection(Utils.dbUrl)) {
            if (conn != null) {
                String insert = "INSERT INTO quiz (title, author) VALUES (?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(insert);
                pstmt.setString(1, title);    
                pstmt.setString(2, author);
                pstmt.executeUpdate();

                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    return new Quiz(id, title, author);
                }
            }
        } catch (SQLException e) {
            System.out.println("DB error: " + e.getMessage());
        }
        return null;
    }


    static void delete(int id) {
        try (Connection conn = DriverManager.getConnection(Utils.dbUrl)) {
            if (conn != null) {
                String deleteQuery = "DELETE FROM quiz WHERE id = ?";
                PreparedStatement pstmt = conn.prepareStatement(deleteQuery);
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("DB error: " + e.getMessage());
        }
    }

    static Quiz get(int id) {
        try (Connection conn = DriverManager.getConnection(Utils.dbUrl)) {
            if (conn != null) {
                String query = "SELECT * FROM quiz WHERE id = ?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, id);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return new Quiz(rs.getInt("id"), rs.getString("title"), rs.getString("author"));
                }
            }
        } catch (SQLException e) {
            System.out.println("DB error: " + e.getMessage());
        }
        return null;
    }

    static void update(int id, String newTitle, String newAuthor) {
        try (Connection conn = DriverManager.getConnection(Utils.dbUrl)) {
            if (conn != null) {
                String updateQuery = "UPDATE quiz SET title = ?, author = ? WHERE id = ?";
                PreparedStatement pstmt = conn.prepareStatement(updateQuery);
                pstmt.setString(1, newTitle);
                pstmt.setString(2, newAuthor);
                pstmt.setInt(3, id);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("DB error: " + e.getMessage());
        }
    }





    public static ArrayList<Quiz> getAll(){
        ArrayList<Quiz> allQuizzes = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(Utils.dbUrl)) {
            if (conn != null) {
                String insert = "SELECT * FROM quiz";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(insert);
                while (rs.next()) {
                    allQuizzes.add(new Quiz(rs.getInt("id"),rs.getString("title"), rs.getString("author")));
                }
            }
        } catch (SQLException e) {
            System.out.println("DB error: " + e.getMessage());
        }
        return allQuizzes;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void addAnswer(Task task, String answer) {
        answers.put(task, answer);
    }

    public Task getTask(int index) {
        return tasks.get(index);
    }

    public int numberOfTasks() {
        return tasks.size();
    }

    public boolean isCompleted() {
        return answers.size() == tasks.size();
    }

    public int checkAnswers() {
        int correct = 0;
        for (Map.Entry<Task, String> entry : answers.entrySet()) {
            if (entry.getKey().checkAnswer(entry.getValue())) {
                correct++;
            }
        }
        return correct;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public Map<Task, String> getAnswers() {
        return answers;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public void clearAnswers() {
        answers.clear();
    }

}
