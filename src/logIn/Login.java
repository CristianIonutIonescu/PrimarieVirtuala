package logIn;

import globals.username;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import mainPage.MainPage;

public class Login {
	private static String username;
 public static JFrame frame;
 	public Login(){
 		frame = new JFrame("Welcome - Sign In");
		frame.setLayout(new GridLayout(3,1));
		frame.setSize(500,500);
		frame.setVisible(true);
		frame.add(new JPanel());
		Username();
		JPanel panel = new JPanel();
		frame.add(panel);
		JButton LoginInButton = new JButton("Login");
		panel.add(LoginInButton);
		
		LoginInButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				frame.setVisible(false);
				username = textFieldUserName.getText();
				new MainPage(username);
			}
		});

		JButton RegisterButton = new JButton("Register");
		panel.add(RegisterButton);
       
		RegisterButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				new Register();
			}
		});
		
		JButton CancelButton = new JButton("Cancel");
		panel.add(CancelButton);
		
		frame.revalidate();
		frame.invalidate();
		frame.repaint();
 	}
	public static void main(String s[]) {
		
		 new Login();
	}

	public static void Username() {
		
		JLabel labelText = new JLabel("Sign In", JLabel.CENTER);
		labelText.setFont(new Font("Comic Sans MS", Font.PLAIN, 26));
		labelText.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		
		JPanel panouContainer = new JPanel();
		frame.add(panouContainer,BorderLayout.NORTH);
		
		JPanel panouUserName = new JPanel();
		JLabel labelUserName = new JLabel("User Name", JLabel.LEFT);
		panouUserName.setLayout(new FlowLayout());
		panouUserName.add(labelUserName );
		textFieldUserName = new JTextField(20);
		panouUserName.add(textFieldUserName);
		
		JPanel panouPassword = new JPanel();
		JLabel labelPassword = new JLabel("Password ", JLabel.LEFT);
		panouPassword.setLayout(new FlowLayout());
		panouPassword.add(labelPassword);
		JPasswordField textFieldPassword = new JPasswordField(20);
		panouPassword.add(textFieldPassword);
	
		panouContainer.setLayout(new GridLayout(4,1));
		panouContainer.add(labelText);
		panouContainer.add(panouUserName);
		panouContainer.add(panouPassword);
		
		frame.revalidate();
		frame.invalidate();
		frame.repaint();
	}
	
	private static JTextField textFieldUserName;

}
