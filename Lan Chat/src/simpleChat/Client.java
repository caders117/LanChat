package simpleChat;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Client
 * @author Cade
 *
 */
public class Client extends ChatObject {
	
	private String addr = "localhost";
	private int port = 8080;
	private Socket socket;
	private boolean closed;
	
	public Client(String address, int port) throws UnknownHostException, IOException {
		addr = address;
		this.port = port;
	}
	
	public Client() {
		
	}
	
	public void setAddress(String a) {
		addr = a;
	}
	
	public void setPort(int p) {
		port = p;
	}
	
	public void startListening() throws IOException {
		socket = new Socket(addr, port);
		closed = false;
		System.out.println("Connection made at: " + addr + ", port " + port);
		// Separate thread for reading input
		ClientInput inputThread = new ClientInput(socket, this);
		inputThread.start();
	}
	
	/**
	 * Writes a string to the output stream of this socket
	 * @param msg
	 */
	public void send(String msg) {
		if(!closed) {
			DataOutputStream output;
			try {
				output = new DataOutputStream(socket.getOutputStream());
				output.writeUTF(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public boolean isClosed() {
		return closed;
	}
	
	public void close() throws IOException {
		socket.close();
		closed = true;
		System.out.println("Client closed");
	}
}
