package broadcastChat;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;

import simpleChat.InputListener;

public class BroadcastClientTest implements InputListener, ServerListener {

	static BroadcastClient client = null;
	static int ID = 0;
	
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		try {
			client = new BroadcastClient();
			client.setName("client" + ID++);
			client.addInputListener(new BroadcastClientTest());
			client.addServerListener(new BroadcastClientTest());
			client.broadcast("This is client.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String msg = "";
		while(!msg.equals(".close")) {
			msg = scan.nextLine();
			client.send(client.getName() + ">" + msg);
		}
		try {
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void serverFound(String addr, int port) {
		// TODO Auto-generated method stub
		System.out.println("Fount a serveer");
		try {
			client.connect(addr, port);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void messageReceived(String msg) {
		// TODO Auto-generated method stub
		System.out.println(msg);
	}
	
}
