import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QuizEditPage extends JFrame {
    private JTextField titleField, authorField;
    private JTextField taskTitleField, descriptionField, answerField, pointsField;
    private DefaultListModel<String> taskListModel;
    private Quiz quiz;

    public QuizEditPage(Quiz quiz) {
        this.quiz = quiz;

        setTitle("Edit Quiz - " + quiz.getTitle());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        // --- Top: Quiz Info ---
        JPanel quizInfoPanel = new JPanel(new GridLayout(3, 2, 5, 5)); // 2 rows, 2 columns
        quizInfoPanel.setBorder(BorderFactory.createTitledBorder("Quiz Info"));

        titleField = new JTextField(quiz.getTitle());
        authorField = new JTextField(quiz.getAuthor());
        JButton updateButton = new JButton("Update");
        quizInfoPanel.add(new JLabel("Title:"));
        quizInfoPanel.add(titleField);
        quizInfoPanel.add(new JLabel("Author:"));
        quizInfoPanel.add(authorField);
        quizInfoPanel.add(new JLabel());
        quizInfoPanel.add(updateButton);

        // --- Center: Task Addition ---
        JPanel taskPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        taskPanel.setBorder(BorderFactory.createTitledBorder("Add New Task"));

        taskTitleField = new JTextField();
        descriptionField = new JTextField();
        answerField = new JTextField();
        pointsField = new JTextField();

        taskPanel.add(new JLabel("Task Title:"));
        taskPanel.add(taskTitleField);
        taskPanel.add(new JLabel("Description:"));
        taskPanel.add(descriptionField);
        taskPanel.add(new JLabel("Answer:"));
        taskPanel.add(answerField);
        taskPanel.add(new JLabel("Points:"));
        taskPanel.add(pointsField);

        JButton addTaskButton = new JButton("Add Task");
        JButton menuButton = new JButton("Menu");

        taskPanel.add(menuButton); 
        taskPanel.add(addTaskButton);

        // --- Bottom: Task List ---
        JPanel taskListPanel = new JPanel(new BorderLayout());
        taskListPanel.setBorder(BorderFactory.createTitledBorder("Tasks"));

        taskListModel = new DefaultListModel<>();
        JList<String> taskList = new JList<>(taskListModel);
        JScrollPane scrollPane = new JScrollPane(taskList);
        taskListPanel.add(scrollPane, BorderLayout.CENTER);

        // Add existing tasks
        for (Task task : quiz.getTasks()) {
            taskListModel.addElement(task.getTitle());
        }

        updateButton.addActionListener(e -> {
            Quiz.update(quiz.getId(), titleField.getText(), authorField.getText());
        });

        menuButton.addActionListener(e -> {
            dispose(); 
            new MenuPage();
        });

        // Add functionality to the Add Task button
        addTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String taskTitle = taskTitleField.getText().trim();
                    String description = descriptionField.getText().trim();
                    String answer = answerField.getText().trim();
                    int points = Integer.parseInt(pointsField.getText().trim());

                    if (taskTitle.isEmpty() || description.isEmpty() || answer.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please fill all task fields!");
                        return;
                    }

                    Task task = Task.add(taskTitle, description, answer, points, quiz.getId());
                    quiz.addTask(task);

                    taskListModel.addElement(taskTitle);

                    taskTitleField.setText("");
                    descriptionField.setText("");
                    answerField.setText("");
                    pointsField.setText("");

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Points must be an integer!");
                }
            }
        });

        panel.add(quizInfoPanel, BorderLayout.NORTH);
        panel.add(taskPanel, BorderLayout.CENTER);
        panel.add(taskListPanel, BorderLayout.SOUTH);

        add(panel);
        setVisible(true);
    }
}
