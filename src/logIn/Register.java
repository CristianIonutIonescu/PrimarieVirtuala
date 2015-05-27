package logIn;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Register {
	public static JFrame frame;

	public Register() {
		
		frame = new JFrame("Register - Sign Up");
		frame.setSize(500, 650);
		frame.setVisible(true);
		
		Username();
		JPanel panel = new JPanel();
		frame.add(panel);
		JButton button = new JButton("OK");
		panel.add(button);

		JButton button2 = new JButton("Cancel");
		panel.add(button2);

		frame.revalidate();
		frame.invalidate();
		frame.repaint();
	}
	

	public static void Username() {
		JPanel panouContainer = new JPanel();
		frame.add(panouContainer, BorderLayout.NORTH);

		JPanel panouText = new JPanel();
		JLabel labelText = new JLabel("Sign Up", JLabel.CENTER);
		labelText.setFont(new Font("Comic Sans MS", Font.PLAIN, 32));
		labelText.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		

		JPanel panouFName = new JPanel();
		JLabel labelFName = new JLabel("First Name          ", JLabel.LEFT);
		panouFName.setLayout(new FlowLayout());
		panouFName.add(labelFName);
		JTextField textFieldFName = new JTextField(20);
		panouFName.add(textFieldFName);

		JPanel panouLName = new JPanel();
		JLabel labelLName = new JLabel("Last Name           ", JLabel.LEFT);
		panouLName.setLayout(new FlowLayout());
		panouLName.add(labelLName);
		JTextField textFieldName = new JTextField(20);
		panouLName.add(textFieldName);

		
		JPanel panouUserName = new JPanel();
		JLabel labelUserName = new JLabel("UserName           ", JLabel.LEFT);
		panouUserName.setLayout(new FlowLayout());
		panouUserName.add(labelUserName);
		JTextField textFieldUserName = new JTextField(20);
		panouUserName.add(textFieldUserName);

		JPanel panouPassword = new JPanel();
		JLabel labelPassword = new JLabel("Password             ", JLabel.LEFT);
		panouPassword.setLayout(new FlowLayout());
		panouPassword.add(labelPassword);
		JPasswordField textFieldPassword = new JPasswordField(20);
		panouPassword.add(textFieldPassword);
		
		JPanel panouRPassword = new JPanel();
		JLabel labelRPassword = new JLabel("Repeat Password", JLabel.LEFT);
		panouRPassword.setLayout(new FlowLayout());
		panouRPassword.add(labelRPassword);
		JPasswordField textFieldRPassword = new JPasswordField(20);
		panouRPassword.add(textFieldRPassword);

		panouContainer.setLayout(new GridLayout(6,1));
		
		
		panouContainer.add(labelText);
		panouContainer.add(panouFName);
		panouContainer.add(panouLName);
		panouContainer.add(panouUserName);
		panouContainer.add(panouPassword);
		panouContainer.add(panouRPassword);
		

		frame.revalidate();
		frame.invalidate();
		frame.repaint();
	}

}