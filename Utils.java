import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.util.stream.Stream;


class Utils{

	public int WIDTH = 700;
	public int HEIGTH = 500;
    static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 24);
    static final Font SUBTITLE_FONT = new Font("SansSerif", Font.PLAIN, 16);
    static final Font BUTTON_FONT = new Font("SansSerif", Font.PLAIN, 14);
    static private final String filePath = "Sessions.txt";
    static String session = getSession();


	JFrame frame = new JFrame();
	ImageIcon logo = new ImageIcon("static/logo.png");
	static String dbUrl = "jdbc:sqlite:quiz.db";

	
	JFrame getDefaultFrame() {

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH,HEIGTH);
		frame.setTitle("QuizLand");
		frame.setIconImage(logo.getImage());
		frame.getContentPane().setBackground(new Color(34,33,32));
		return frame;

	}


	static void setUpDataBase() {

        // 1. Connect to database
        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            if (conn != null) {
                System.out.println("Connected to SQLite database.");

                // 2. Create table
                String createQuizTable = "CREATE TABLE IF NOT EXISTS quiz (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "title TEXT NOT NULL," +
                        "author TEXT" +
                        ");";

                String createUserTable = "CREATE TABLE IF NOT EXISTS user (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "username TEXT NOT NULL," +
                        "secret_key TEXT NOT NULL" +
                        ");";

                String createTaskTable = "CREATE TABLE IF NOT EXISTS task (" +  
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "title TEXT NOT NULL," +
                        "description TEXT NOT NULL," +
                        "answer TEXT NOT NULL," +
                        "points INTEGER NOT NULL," +
                        "quiz_id INTEGER," +
                        "FOREIGN KEY (quiz_id) REFERENCES quiz(id)" +
                        ");";

                Statement stmt = conn.createStatement();
                stmt.execute(createUserTable);
                stmt.execute(createQuizTable);
                stmt.execute(createTaskTable);
            }
        } catch (SQLException e) {
            System.out.println("DB error: " + e.getMessage());
        }
    }

    


    static private String generateSession(String username, String secretKey) {
        return String.valueOf(Objects.hash(username, secretKey));
    }

    static void setSession(String username, String secretKey) {
        try {
            File file = new File(filePath);
            if (!file.exists()) file.createNewFile();

            String sessionData = generateSession(username, secretKey) + "\n";

            Files.write(Paths.get(filePath),
                    sessionData.getBytes(),
                    StandardOpenOption.WRITE);

        } catch (IOException ex) {
            System.err.println("Error creating session: " + ex.getMessage());
        }
    }

    static String getSession() {
        try {
            File file = new File(filePath);
            if (!file.exists()) file.createNewFile();

            try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
                return lines.findFirst().orElse(null);
            }

        } catch (IOException ex) {
            System.err.println("Error getting session: " + ex.getMessage());
            return null;
        }
    }
}



