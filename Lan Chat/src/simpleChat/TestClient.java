package simpleChat;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class TestClient implements InputListener {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		Client client = null;
		try {
			client = new Client("192.168.2.131", 8080);
			client.startListening();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		client.addInputListener(new TestClient());
		String msg = "";
		while(!msg.equals(".close") && !client.getSocket().isClosed()) {
			msg = scan.nextLine();
			client.send(msg);
		}
		try {
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		scan.close();
	}

	@Override
	public void messageReceived(String msg) {
		// TODO Auto-generated method stub
		System.out.println(msg);
	}
}
