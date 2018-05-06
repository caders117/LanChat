package simpleChat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * New thread for listening for clients connecting
 * to the current server.  Once a client is connected,
 * the client is added to the server's list of connections.
 * 
 * @author Cade
 *
 */
public class ServerThread extends Thread {
	
	protected ServerSocket serverSoc;
	protected Server server;
	
	public ServerThread(ServerSocket serSoc, Server ser) {
		serverSoc = serSoc;
		server = ser;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(!server.getServerSocket().isClosed()) {
			try {
				listenForConnection();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("ServerSocket closed");
				break;
			}
		}
	}
	
	/**
	 * Blocks until ServerSocket finds new connection.
	 * Once connection is made, it adds that socket to
	 * the list of connections in the server.  Then it
	 * starts a new thread to listen for/read input from
	 * client.
	 * @throws IOException
	 */
	private void listenForConnection() throws IOException {
		Socket socket = serverSoc.accept();
		System.out.println("New connection made at " + socket.getInetAddress() + ", port " + socket.getPort());
		server.addConnection(socket);
		ServerInput inputThread = new ServerInput(socket, server);
		inputThread.start();
	}
}
