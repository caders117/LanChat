package broadcastChat;

import java.io.IOException;
import java.net.SocketException;
import java.util.Scanner;

import simpleChat.InputListener;

public class BroadcastServerTest implements InputListener {
	
	static BroadcastServer server = null;
	static int ID = 0;
	
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		try {
			server = new BroadcastServer();
			server.setName("Server");
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		server.addInputListener(new BroadcastServerTest());
		
		String msg = "";
		while(!msg.equals(".close")) { 
			msg = scan.nextLine();
			server.sendToAllClients(server.getName() + "> " + msg);
		}
		try {
			server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.exit(0);
	}

	@Override
	public void messageReceived(String msg) {
		// TODO Auto-generated method stub
		System.out.println(msg);
		server.sendToAllClients(msg);
	}
}
