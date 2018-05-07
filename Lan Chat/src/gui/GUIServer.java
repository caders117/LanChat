package gui;

import java.io.IOException;
import java.net.SocketException;

import broadcastChat.BroadcastServer;
import simpleChat.InputListener;

public class GUIServer extends BroadcastServer {

	public GUIServer() throws SocketException {	}
	
	public GUIServer(int port) throws IOException {
		super(port);
	}
	
	public GUIServer(String n) throws IOException {
		super(n);
	}
	
	public GUIServer(int port, String n) throws IOException {
		super(port, n);
	}
	
	{
		addInputListener(new Listener());
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
			System.out.println(chatLog + msg);
			chatLog = chatLog + msg;
			System.out.println(chatLog);
			sendToAllClients(chatLog);
		}
	}
}
