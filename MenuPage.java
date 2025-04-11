import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

public class MenuPage {
    ImageIcon homeImage = new ImageIcon("static/Home.png");
    JFrame frame = new Utils().getDefaultFrame();
    JLabel label = new JLabel("Welcome To QuizLand");
    JPanel cardPanel = new JPanel();
    JPanel buttonPanel = new JPanel();
    JLabel welcomeLabel = new JLabel("🎓 Welcome to the Quiz App!", SwingConstants.CENTER);
    JPanel panel = new JPanel(new BorderLayout());

    MenuPage() {
        frame.setVisible(true);

        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        welcomeLabel.setFont(Utils.TITLE_FONT);
        panel.add(welcomeLabel, BorderLayout.NORTH);

        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));

        cardPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        cardPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        addAllQuiz(cardPanel);

        JScrollPane scrollPane = new JScrollPane(cardPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        buttonPanel.add(scrollPane);
        panel.add(buttonPanel, BorderLayout.CENTER);

        frame.add(panel);
    }

    void addAllQuiz(JPanel buttonPanel) {
        ArrayList<Quiz> allQuizzes = Quiz.getAll();

        for (Quiz q : allQuizzes) {
            final String finalTitle = q.getTitle();
            final int finalId = q.getId();

            JPanel quizCard = new JPanel();
            quizCard.setLayout(new BoxLayout(quizCard, BoxLayout.Y_AXIS));
            quizCard.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            quizCard.setBackground(Color.WHITE);
            quizCard.setPreferredSize(new Dimension(200, 200));
            quizCard.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel titleLabel = new JLabel(String.format("Quiz: %s", finalTitle));
            titleLabel.setFont(Utils.SUBTITLE_FONT);
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JButton startQuizButton = new JButton("📝 Start Quiz");
            startQuizButton.setFont(Utils.SUBTITLE_FONT);
            startQuizButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            startQuizButton.addActionListener(e -> {
                Quiz quiz = chooseQuiz(finalId);
                if (quiz != null) {
                    new QuizPage(quiz);
                } else {
                    JOptionPane.showMessageDialog(null, "Quiz not found!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            JButton editQuizButton = new JButton("✏️ Edit Quiz");
            editQuizButton.setFont(Utils.SUBTITLE_FONT);
            editQuizButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            editQuizButton.addActionListener(e -> {
                frame.dispose();
                new QuizEditPage(q);
            });

            JButton removeQuizButton = new JButton("🗑️ Remove");
            removeQuizButton.setFont(Utils.SUBTITLE_FONT);
            removeQuizButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            removeQuizButton.addActionListener(e -> {
                Quiz.delete(finalId);
                updateMenu();
            });

            JButton exportButton = new JButton("⬇️ Export CSV");
            exportButton.setFont(Utils.SUBTITLE_FONT);
            exportButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            exportButton.addActionListener(e -> exportQuizToCSV(q));

            quizCard.add(Box.createVerticalStrut(10));
            quizCard.add(titleLabel);
            quizCard.add(Box.createVerticalStrut(10));
            quizCard.add(startQuizButton);
            quizCard.add(Box.createVerticalStrut(10));

            if (Utils.session != null && Utils.session.equals(q.getAuthor())) {
                quizCard.add(editQuizButton);
                quizCard.add(removeQuizButton);
                quizCard.add(exportButton);
            }

            cardPanel.add(Box.createVerticalStrut(10));
            cardPanel.add(quizCard);
        }

        JButton createQuizButton = new JButton("➕ Create New Quiz");
        createQuizButton.setFont(Utils.SUBTITLE_FONT);
        createQuizButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        createQuizButton.addActionListener(e -> openCreateQuizDialog());

        JButton importQuizButton = new JButton("⬆️ Import Quiz CSV");
        importQuizButton.setFont(Utils.SUBTITLE_FONT);
        importQuizButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        importQuizButton.addActionListener(e -> importQuizFromCSV());

        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(createQuizButton);
        buttonPanel.add(importQuizButton);
    }

    Quiz chooseQuiz(int id) {
        Quiz quiz = Quiz.get(id);
        return quiz;
    }

    private void openCreateQuizDialog() {
        frame.setEnabled(false);
        JDialog dialog = new JDialog((JFrame) null, "Create New Quiz", true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new GridLayout(3, 1, 10, 10));

        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                frame.setEnabled(true);
            }
        });

        JTextField titleField = new JTextField();
        dialog.add(new JLabel("Quiz Title:"));
        dialog.add(titleField);

        JButton createButton = new JButton("✅ Create");
        createButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please enter a quiz title.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String author = Utils.getSession();
            Quiz.add(title, author);
            JOptionPane.showMessageDialog(dialog, "Quiz created successfully!");
            dialog.dispose();
            updateMenu();
        });

        dialog.add(createButton);
        dialog.setVisible(true);
    }

    private void exportQuizToCSV(Quiz quiz) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File(quiz.getTitle() + ".csv"));
        int option = fileChooser.showSaveDialog(frame);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (PrintWriter writer = new PrintWriter(file)) {
                writer.println("title,description,answer,points,quiz_id");
                for (Task task : quiz.getTasks()) {
                    writer.printf("\"%s\",\"%s\",\"%s\",%d\n",
                            task.getTitle(), task.getDescription(), task.getAnswer(), task.getPoints());
                }
                JOptionPane.showMessageDialog(frame, "Quiz exported successfully!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Error exporting quiz: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void importQuizFromCSV() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(frame);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String quizTitle = JOptionPane.showInputDialog(frame, "Enter title for imported quiz:");
            if (quizTitle == null || quizTitle.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Invalid title!");
                return;
            }

            Quiz newQuiz = Quiz.add(quizTitle, Utils.getSession());
            if (newQuiz == null) {
                JOptionPane.showMessageDialog(frame, "Failed to create quiz in database.");
                return;
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                boolean skipHeader = true;
                while ((line = reader.readLine()) != null) {
                    if (skipHeader) {
                        skipHeader = false;
                        continue;
                    }

                    String[] parts = line.split("\",?\"?");
                    if (parts.length >= 4) {
                        String title = parts[1].replace("\"", "").trim();
                        String description = parts[2].replace("\"", "").trim();
                        String answer = parts[3].replace("\"", "").trim();
                        int points = Integer.parseInt(parts[4].trim());
                        Task task = Task.add(title, description, answer, points, newQuiz.getId());
                        newQuiz.addTask(task);
                    }
                }
                JOptionPane.showMessageDialog(frame, "Quiz imported successfully!");
                updateMenu();
            } catch (IOException | NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Error importing quiz: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    void updateMenu() {
        SwingUtilities.invokeLater(() -> {
            cardPanel.removeAll();
            addAllQuiz(cardPanel);
            cardPanel.revalidate();
            cardPanel.repaint();
        });
    }
}
