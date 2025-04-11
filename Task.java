import java.util.Objects;
import java.sql.*;

public class Task {
    private final int id;
    private final String title;
    private final String description;
    private final String answer;
    private final int points;

    public Task(int id, String title, String description, String answer, int points) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.answer = answer;
        this.points = points;
    }

    static Task add(String title, String description, String answer, int points, int quiz_id) {
        try (Connection conn = DriverManager.getConnection(Utils.dbUrl)) {
            if (conn != null) {
                String insert = "INSERT INTO task (title, description, answer, points, quiz_id) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, title);    
                pstmt.setString(2, description);
                pstmt.setString(3, answer);    
                pstmt.setInt(4, points);
                pstmt.setInt(5, quiz_id);
                pstmt.executeUpdate();

                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    return new Task(id, title, description, answer, points);
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
                String deleteQuery = "DELETE FROM tasks WHERE id = ?";
                PreparedStatement pstmt = conn.prepareStatement(deleteQuery);
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("DB error: " + e.getMessage());
        }
    }

    static Task get(int id) {
        try (Connection conn = DriverManager.getConnection(Utils.dbUrl)) {
            if (conn != null) {
                String query = "SELECT * FROM task WHERE id = ?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, id);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return new Task(rs.getInt("id"), rs.getString("title"), rs.getString("description"), rs.getString("answer"), rs.getInt("points"));
                }
            }
        } catch (SQLException e) {
            System.out.println("DB error: " + e.getMessage());
        }
        return null;
    }


    public boolean checkAnswer(String input) {
        return answer.equalsIgnoreCase(input.trim());
    }

    public String formatTask() {
        return String.format("%s\n\n%s", title, description);
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAnswer() {
        return answer;
    }

    public int getPoints() {
        return points;
    }

}
