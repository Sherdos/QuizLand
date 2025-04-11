import javax.swing.*;
import java.awt.*;

class QuizPage{

	JFrame frame = new Utils().getDefaultFrame();
	JPanel panel = new JPanel(new BorderLayout(10, 10));
	JTextArea headerTextArea = new JTextArea(5,40);
	JPanel inputPanel = new JPanel();
	JTextField userInputField = new JTextField();
	JLabel statusLabel = new JLabel("", SwingConstants.CENTER);
	JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
    JButton prevButton = new JButton("⏮ Previous");
    JButton submitButton = new JButton("✅ Submit");
    JButton nextButton = new JButton("Next ⏭");
    JButton menuButton = new JButton("Menu");
	int curTask;
	Quiz currentQuiz;

	QuizPage(Quiz currentQuiz) {
		this.currentQuiz = currentQuiz;
		this.curTask = 0;
		
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        headerTextArea.setEditable(false);
        headerTextArea.setFont(Utils.SUBTITLE_FONT);
        headerTextArea.setLineWrap(true);
        headerTextArea.setWrapStyleWord(true);
        panel.add(new JScrollPane(headerTextArea), BorderLayout.NORTH);

        
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        
        userInputField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        userInputField.setFont(Utils.SUBTITLE_FONT);
        userInputField.setAlignmentX(Component.CENTER_ALIGNMENT);

        
        statusLabel.setFont(Utils.SUBTITLE_FONT);
        statusLabel.setForeground(Color.GRAY);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        inputPanel.add(userInputField);
        inputPanel.add(Box.createVerticalStrut(10));
        inputPanel.add(statusLabel);

        panel.add(inputPanel, BorderLayout.CENTER);

        

        prevButton.setFont(Utils.BUTTON_FONT);
        submitButton.setFont(Utils.BUTTON_FONT);
        nextButton.setFont(Utils.BUTTON_FONT);

        menuButton.addActionListener(e -> {
            frame.dispose(); 
        });

        prevButton.addActionListener(e -> {
            if (curTask > 0) {
                curTask--;
                updateUI();
            }
        });

        

        submitButton.addActionListener(e -> {
            String userAnswer = userInputField.getText().trim();
            if (!userAnswer.isEmpty()) {
                this.currentQuiz.addAnswer(this.currentQuiz.getTask(curTask), userAnswer);
                if (curTask < this.currentQuiz.numberOfTasks() - 1) {
                    curTask++;
                }
                updateUI();
            } else {
                statusLabel.setText("Answer cannot be empty!");
            }
        });

        nextButton.addActionListener(e -> {
            if (curTask < this.currentQuiz.numberOfTasks() - 1) {
                curTask++;
                updateUI();
            }
        });

        controlPanel.add(prevButton);
        controlPanel.add(submitButton);
        controlPanel.add(nextButton);
        controlPanel.add(menuButton);

        panel.add(controlPanel, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setVisible(true);
        updateUI();

	}


	void updateUI() {
        if (this.currentQuiz == null || this.currentQuiz.numberOfTasks() == 0) return;

        Task current = this.currentQuiz.getTask(this.curTask);
        this.headerTextArea.setText(current.formatTask());

        String prevAnswer = this.currentQuiz.getAnswers().get(current);
        this.userInputField.setText(prevAnswer != null ? prevAnswer : "");

        if (this.currentQuiz.isCompleted()) {
            this.frame.dispose();
            JOptionPane.showMessageDialog(null, String.format("Congratulations !!! You get %d/%d ", this.currentQuiz.checkAnswers(),this.currentQuiz.numberOfTasks()), "Quiz Completed", JOptionPane.PLAIN_MESSAGE);

        }
    }




}



