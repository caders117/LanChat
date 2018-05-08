package gui;

import java.io.IOException;
import java.net.SocketException;

import broadcastChat.BroadcastServer;
import simpleChat.InputListener;

public class GUIServer extends BroadcastServer {
	
	ClientGUI cligui;
	String clientName = "Test";
	
	public GUIServer() throws SocketException {
		cligui = new ClientGUI(getName(), this.getServerSocket().getInetAddress().getHostAddress(), this.getServerSocket().getLocalPort(), getName());

	}
	
	public GUIServer(int port) throws IOException {
		super(port);
		cligui = new ClientGUI(getName(), this.getServerSocket().getInetAddress().getHostAddress(), this.getServerSocket().getLocalPort(), clientName);

	}
	
	public GUIServer(String n) throws IOException {
		super(n);
		cligui = new ClientGUI(getName(), this.getServerSocket().getInetAddress().getHostAddress(), this.getServerSocket().getLocalPort(), clientName);

	}
	
	public GUIServer(int port, String n) throws IOException {
		super(port, n);
		cligui = new ClientGUI(getName(), this.getServerSocket().getInetAddress().getHostAddress(), this.getServerSocket().getLocalPort(), clientName);

	}
	
	public GUIServer(String n, String s) throws SocketException {
		super(n);
		clientName = s;
		cligui = new ClientGUI(getName(), this.getServerSocket().getInetAddress().getHostAddress(), this.getServerSocket().getLocalPort(), clientName);

	}
	
	{
		addInputListener(new Listener());
	}
	
	public ClientGUI getClientGUI() {
		return cligui;
	}
	
	@Override
	public void close() throws IOException {
		super.close();
		if(cligui != null) cligui.getClient().close();
	}
	
	private String chatLog = "";

	public static void main(String[] args) {
		try {
			GUIServer serverHelp = new GUIServer("test");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	class Listener implements InputListener {
		@Override
		public void messageReceived(String msg) {
		//	System.out.println(chatLog + msg);
			chatLog = chatLog + msg;
		//	System.out.println(chatLog);
			sendToAllClients(chatLog);
		}
	}
}
