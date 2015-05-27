package mainPage;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import chat.ChatMainForm;


public class MainPage {
	public static JFrame frame;

	public MainPage() {
		frame = new JFrame("Main Dialog");
		frame.setLayout(new BorderLayout());
		frame.setSize(600, 700);
		frame.setVisible(true);

		Username();

		JPanel panel1 = new JPanel();

		JButton button = new JButton("Organization Chart");
		button.setPreferredSize(new Dimension(150, 50));

		panel1.add(button);

		JPanel panel2 = new JPanel();
		JButton button2 = new JButton("Chat");
		button2.setPreferredSize(new Dimension(150, 50));
		panel2.add(button2);
		
		button2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				new ChatMainForm();
			}
		});
		
		

		JPanel panel3 = new JPanel();
		JButton button3 = new JButton("Documents");
		button3.setPreferredSize(new Dimension(150, 50));
		panel3.add(button3);

		JPanel panel4 = new JPanel();
		panel4.add(panel1);
		panel4.add(panel2);
		panel4.add(panel3);

		panel4.setLocation(400, 10);
		frame.add(panel4, BorderLayout.SOUTH);

		frame.revalidate();
		frame.invalidate();
		frame.repaint();
	}

	public static void Username() {
		JPanel panouContainer = new JPanel();

		JPanel panouText = new JPanel();
		JLabel labelText = new JLabel("City Hall");
		labelText.setFont(new Font("ARIAL", Font.PLAIN, 32));
		labelText.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 40));

		panouContainer.add(labelText);

		frame.add(panouContainer);

		frame.revalidate();
		frame.invalidate();
		frame.repaint();
	}

}