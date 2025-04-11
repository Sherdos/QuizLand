import javax.swing.*;
import java.awt.*;

class LaunchPage{

	JFrame frame = new Utils().getDefaultFrame();
	JPanel panel = new JPanel();
	JButton submitButton = new JButton("Submit");
	ImageIcon logo = new ImageIcon("static/logo.png");
	JTextField usernameInput = new JTextField();
	JTextField secretKeyInput = new JTextField();
	
	LaunchPage() {

		// Input sizes
        usernameInput.setPreferredSize(new Dimension(250, 45));
        secretKeyInput.setPreferredSize(new Dimension(250, 45));

        // Optional: Add placeholder hints using empty borders
        usernameInput.setBorder(BorderFactory.createTitledBorder("Username"));
        secretKeyInput.setBorder(BorderFactory.createTitledBorder("Secret Key"));

        // Customize inputs
        usernameInput.setFont(new Font("Consolas", Font.PLAIN, 19));	


		// Submit button setup
        submitButton.setPreferredSize(new Dimension(250, 40));
        submitButton.setFocusable(false);
        submitButton.addActionListener(e -> {
        	String username = usernameInput.getText();
        	String secretKey = secretKeyInput.getText();
        	if (!User.checkUser(username, secretKey)){
        		User.add(username, secretKey);
        	}
        	Utils.setSession(username, secretKey);
            frame.dispose(); 
            new MenuPage();  
        });


		// Panel setup
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 40));
        panel.setBackground(new Color(34, 34, 41));
		
		panel.add(usernameInput);
		panel.add(secretKeyInput);
		panel.add(submitButton);

		frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);

	}

}



