package simpleChat;

import java.io.IOException;
import java.net.Socket;

/**
 * Input handler class for Server -- when it detects an error when 
 * reading inputStream, it removes the socket from the list of 
 * connections in the server.
 * 
 * @author Cade
 *
 */
public class ServerInput extends InputHandleThread {
		
	public ServerInput(Socket soc, Server ser) throws IOException {
		super(soc, ser);
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
		((Server) chatObj).removeConnection(socket); 
		System.out.println("Connection closed on client's end.");
	}
}
