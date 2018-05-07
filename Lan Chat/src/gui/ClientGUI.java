package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import broadcastChat.BroadcastClient;
import broadcastChat.ServerListener;
import simpleChat.Client;
import simpleChat.InputListener;

public class ClientGUI extends JPanel {

	static final int PADDING = 40;
	private JTextArea chatLog = new JTextArea();
	private JScrollPane scroll = new JScrollPane(chatLog);
	private JTextField toSend = new JTextField();
	private JButton sendBtn = new JButton("Send");
	private JTextField status = new JTextField("Not connected.");
	private Client client;
	
	public ClientGUI(String serverName, String ip, int port) {
		sendBtn.addActionListener(new Listeners());
		status.setEditable(false);
		chatLog.setEditable(false);
		toSend.addActionListener(new Listeners());
		scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
		
		SpringLayout layout = new SpringLayout();
		this.setLayout(layout);
		this.add(scroll);
		this.add(toSend);
		this.add(sendBtn);
		this.add(status);
		
		// status text field constraints
		layout.putConstraint(SpringLayout.WEST, status, 
				PADDING, 
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, status, 
				PADDING, 
				SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.EAST, this, 
				PADDING, 
				SpringLayout.EAST, status);
		
		// ChatLog text field constraints
		layout.putConstraint(SpringLayout.WEST, scroll, 
				0, 
				SpringLayout.WEST, status);
		layout.putConstraint(SpringLayout.NORTH, scroll, 
				0, 
				SpringLayout.SOUTH, status);
		layout.putConstraint(SpringLayout.EAST, status, 
				0, 
				SpringLayout.EAST, scroll);
		layout.putConstraint(SpringLayout.SOUTH, scroll, 
				-PADDING, 
				SpringLayout.NORTH, toSend);

		// toSend text field constraints
		layout.putConstraint(SpringLayout.WEST, toSend, 
				PADDING, 
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.SOUTH, toSend, 
				-PADDING, 
				SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.EAST, toSend, 
				-5, 
				SpringLayout.WEST, sendBtn);
		layout.putConstraint(SpringLayout.NORTH, toSend, 
				0, 
				SpringLayout.NORTH, sendBtn);

		// Send button constraints
		layout.putConstraint(SpringLayout.SOUTH, sendBtn, 
				0, 
				SpringLayout.SOUTH, toSend);
		layout.putConstraint(SpringLayout.EAST, sendBtn, 
				0, 
				SpringLayout.EAST, scroll);

		try {
			client = new Client(ip, port);
			status.setText("Connected to " + serverName + " -- " + ip + ":" + port);
			status.setBackground(Color.WHITE);
			client.startListening();
			client.addInputListener(new Listeners());
			System.out.println("Starting broadcast");
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Client getClient() {
		return client;
	}
	
	private class Listeners implements ActionListener, InputListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			String message = toSend.getText();
			client.send(client.getName() + "> " + message + "\n");
			toSend.setText("");
		}

		@Override
		public void messageReceived(String msg) {
			// TODO Auto-generated method stub
			chatLog.setText(msg);
			JScrollBar scrollBar = scroll.getVerticalScrollBar();
			scrollBar.setValue(scrollBar.getMaximum());
		}
	}
}
