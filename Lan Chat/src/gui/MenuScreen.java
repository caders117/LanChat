package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

	private String username = "Username";
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
		MenuScreen menu = new MenuScreen("Cade Test");
		frame.getContentPane().add(menu, BorderLayout.CENTER);
		frame.setVisible(true);
	}
	
	public MenuScreen(String usr) {
		
		username = usr;
		
		serverList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		serverList.setVisibleRowCount(-1);
		
		serverList.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
			      selected = serverList.getSelectedIndex();
			      System.out.println(selected);
			}
		});
		
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
			client.setName(usr);
//			client.broadcastDiscovery();
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
	
	private int selected = serverList.getSelectedIndex();
		
	private class ContinuousListen extends Thread {
		
		BroadcastClient client;
		ArrayList<String> servers = new ArrayList<String>();
		ArrayList<String> addrs = new ArrayList<String>();
		
		public ContinuousListen(BroadcastClient cli) {
			client = cli;
			client.addServerListener(new ContinuousListenForServers());
		}
		
		@Override
		public void run() {
			while(true) {
			try {
				servers.clear();
				client.broadcastDiscovery();
				Thread.sleep(100);
				addresses = addrs;
				availableServers.clear();
				for(String s : servers)
					availableServers.addElement(s);
				if(selected >= availableServers.getSize())
					selected = availableServers.getSize() - 1;
				System.out.println(selected);

				serverList.setSelectedIndex(selected);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		}
		
		private class ContinuousListenForServers implements ServerListener {
			
			@Override
			public void serverFound(String addr, int port) {
				addrs.add(addr + ":" + port);
			}

			@Override
			public void udpMessageReceived(String msg) {
				servers.add(msg);
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
				ClientGUI cligui = new ClientGUI(availableServers.getElementAt(serverList.getSelectedIndex()).toString(), ip, port, username);
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
				
				GUIServer newServer;
				try {
					newServer = new GUIServer(name, client.getName());
					client.broadcastDiscovery();
					
					JFrame frame = new JFrame("Client");
					frame.setSize(500, 500);
					frame.getContentPane().add(newServer.getClientGUI(), BorderLayout.CENTER);
					frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
					frame.addWindowListener(new WindowAdapter() {
						
						@Override
						public void windowClosing(WindowEvent e) {
							int doClose = JOptionPane.showConfirmDialog(null, "Are you sure you want to disband this server?");
							if(doClose == JOptionPane.YES_OPTION) {
								try {
									newServer.close();
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								frame.dispose();
							}
						}
					});
					frame.setVisible(true);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
					
			}
		}
	}
}
