package simpleChat;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends ChatObject {

	ServerSocket serverSoc;
	List<Socket> connections;
	protected static int port = 8080;

	public Server(int port) throws IOException {
		serverSoc = new ServerSocket(port);
		connections = new ArrayList<Socket>();
		System.out.println("ServerSocket started at port " + port);
	}
	
	public Server() {
		while(true) {
			try {
				serverSoc = new ServerSocket(port);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println(port + " not available for use.");
				port++;
				continue;
			}
			break;
		}
		connections = new ArrayList<Socket>();
		System.out.println("ServerSocket started at port " + port);
	}
	
	/**
	 * Starts listening for connections.  Starts
	 * a new input listener thread for each new connection.
	 */
	public void start() {
		ServerThread s = new ServerThread(serverSoc, this);
		s.start();
	}
	
	/**
	 * Broadcasts a message to all clients connected
	 * to this socket.
	 * 
	 * @param msg - String to be sent
	 */
	public void sendToAllClients(String msg) {
		DataOutputStream output;
		for(Socket s : connections) {
			try {
				output = new DataOutputStream(s.getOutputStream());
				output.writeUTF(msg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void addConnection(Socket s) {
		connections.add(s);
	}
	
	public void removeConnection(Socket s) {
		connections.remove(s);
	}
	
	public List<Socket> getConnections() {
		return connections;
	}
	
	public ServerSocket getServerSocket() {
		return serverSoc;
	}
	
	public void close() throws IOException {
		serverSoc.close();
	}
}
