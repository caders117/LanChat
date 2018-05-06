package simpleChat;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Contains fields and methods shared between
 * the server and client input handler classes.
 * Not sure if I need this.
 * 
 * @author Cade
 *
 */
public abstract class InputHandleThread extends Thread {

	protected ChatObject chatObj;
	protected Socket socket;
	private DataInputStream input;

	public InputHandleThread(Socket soc, ChatObject c) throws IOException {
		chatObj = c;
		socket = soc;
		input = new DataInputStream(socket.getInputStream());
	}

	/**
	 * Reads input for socket.
	 * @throws IOException
	 */
	protected void readInput() throws IOException {
		String msg = "";
		msg = input.readUTF();
		chatObj.receiveMessage(msg);
	}
}
