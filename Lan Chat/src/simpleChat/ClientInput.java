package simpleChat;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

/**
 * Input handler class for Client -- closes the 
 * client's socket if the server is closed unexpectedly.
 * 
 * @author Cade
 *
 */
public class ClientInput extends InputHandleThread {
	
	public ClientInput(Socket soc, Client cli) throws IOException {
		super(soc, cli);
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				readInput();
			} catch (IOException e) {
				break;
			}
		}
		try {
			socket.close();
			((Client) chatObj).close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(((Client) chatObj).isClosed())
			System.out.println("Client socket closed");
		else
			System.out.println("Server closed causing this client to close.");
	}
}
