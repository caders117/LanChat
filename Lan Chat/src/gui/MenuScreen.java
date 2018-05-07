package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;

import broadcastChat.BroadcastClient;
import broadcastChat.ServerListener;

public class MenuScreen extends JPanel {
	
	static final int PADDING = 40;

	private ArrayList<String> addresses = new ArrayList<String>();
    private DefaultListModel availableServers = new DefaultListModel();;
	private JList serverList = new JList(availableServers);
	private JScrollPane scroll = new JScrollPane(serverList);
	private JButton create = new JButton("Create Server");
	private JButton join = new JButton("Join Server");
	private JLabel title = new JLabel("Available Servers: ");

	private static BroadcastClient client;
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Menu");
		frame.setSize(500, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MenuScreen menu = new MenuScreen();
		frame.getContentPane().add(menu, BorderLayout.CENTER);
		frame.setVisible(true);		
	}
	
	public MenuScreen() {
		
		serverList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		serverList.setVisibleRowCount(-1);
		
		create.addActionListener(new CreateServer());
		join.addActionListener(new JoinServer());
		
		SpringLayout layout = new SpringLayout();
		setLayout(layout);
		this.add(title);
		this.add(scroll);
		this.add(create);
		this.add(join);

		// title constraints
		layout.putConstraint(SpringLayout.WEST, title, 
				PADDING, 
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, title, 
				PADDING, 
				SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.EAST, this, 
				PADDING, 
				SpringLayout.EAST, title);

		// list constraints
		layout.putConstraint(SpringLayout.WEST, scroll, 
				0, 
				SpringLayout.WEST, title);
		layout.putConstraint(SpringLayout.NORTH, scroll, 
				0, 
				SpringLayout.SOUTH, title);
		layout.putConstraint(SpringLayout.EAST, title, 
				0, 
				SpringLayout.EAST, scroll);
		layout.putConstraint(SpringLayout.SOUTH, scroll, 
				-PADDING, 
				SpringLayout.NORTH, create);

		// toSend text field constraints
		layout.putConstraint(SpringLayout.WEST, create, 
				PADDING, 
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.SOUTH, create, 
				-PADDING, 
				SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.EAST, create, 
				-PADDING / 2, 
				SpringLayout.HORIZONTAL_CENTER, this);

		// join button constraints
		layout.putConstraint(SpringLayout.EAST, join, 
				-PADDING, 
				SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.SOUTH, join, 
				-PADDING, 
				SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.WEST, join, 
				PADDING / 2, 
				SpringLayout.HORIZONTAL_CENTER, this);

		try {
			client = new BroadcastClient();
			client.addServerListener(new Listener());
			client.broadcastDiscovery();
			ContinuousListen listen = new ContinuousListen(client);
			listen.start();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private class ContinuousListen extends Thread {
		
		BroadcastClient client;
		
		public ContinuousListen(BroadcastClient cli) {
			client = cli;
		}
		
		@Override
		public void run() {
			while(true) {
			try {
				client.broadcastDiscovery();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		}
	}
	
	private class JoinServer implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			int i = serverList.getSelectedIndex();
			if(i >= 0) {
				String addy = addresses.get(i);
				String ip = addy.split(":")[0];
				int port = Integer.parseInt(addy.split(":")[1]);
				JFrame frame = new JFrame("Client");
				frame.setSize(500, 500);
				ClientGUI cligui = new ClientGUI(availableServers.getElementAt(serverList.getSelectedIndex()).toString(), ip, port);
				frame.getContentPane().add(cligui, BorderLayout.CENTER);
				frame.addWindowListener(new WindowAdapter() {
					
					@Override
					public void windowClosing(WindowEvent e) {
						try {
							cligui.getClient().close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				});
				frame.setVisible(true);
			}
		}
	}
	
	private class CreateServer implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
			String name = JOptionPane.showInputDialog(null, "Server name: ", "Create a Server", JOptionPane.QUESTION_MESSAGE);
			System.out.println(name.trim());
			if(name != null) {
				while(name.trim().equals("")) {
					JOptionPane.showMessageDialog(null, "Invalid input.  Try again.", "Invalid", JOptionPane.ERROR_MESSAGE);
					name = JOptionPane.showInputDialog(null, "Server name: ", "Create a Server", JOptionPane.QUESTION_MESSAGE);
					System.out.println(name.trim());
				}
				
				try {
					GUIServer newServer = new GUIServer(name);
					client.broadcastDiscovery();
					int lastIndex = serverList.getModel().getSize() - 1;
					if (lastIndex >= 0) {
					   serverList.ensureIndexIsVisible(lastIndex);
					}
					serverList.setSelectedIndex(lastIndex);
					join.doClick();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
	
	private class Listener implements ServerListener {

		@Override
		public void serverFound(String addr, int port) {
			addresses.add(addr + ":" + port);
		}

		@Override
		public void udpMessageReceived(String msg) {
			if(!availableServers.contains(msg))
				availableServers.addElement(msg);
			System.out.println("Message:" + msg);
		}
	}
}
