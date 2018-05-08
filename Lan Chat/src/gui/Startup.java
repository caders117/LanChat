package gui;

import java.awt.BorderLayout;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Startup {
	
	public static void main(String[] args) {
		String compName = "Username";
		try {
			compName = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String name = JOptionPane.showInputDialog(null, "Enter username:", "Startup", JOptionPane.QUESTION_MESSAGE, null, null, compName).toString();
		MenuScreen menu = new MenuScreen(name);
		JFrame frame = new JFrame("Chat Application");
		frame.setSize(500, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(menu, BorderLayout.CENTER);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
