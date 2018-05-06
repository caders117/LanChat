package simpleChat;

import java.io.IOException;
import java.util.Scanner;

public class TestServer implements InputListener {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		
		Server server = null;
		try {
			server = new Server(8080);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		server.addInputListener(new TestServer());
		server.start();
		
		String msg = "";
		while(msg != ".close" && !server.getServerSocket().isClosed()) {
			msg = scan.nextLine();
			server.broadcast(msg);
		}
		try {
			server.close();
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
