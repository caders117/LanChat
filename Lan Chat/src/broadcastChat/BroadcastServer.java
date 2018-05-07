package broadcastChat;

import java.io.IOException;
import java.net.SocketException;

import simpleChat.Server;

public class BroadcastServer extends Server {
	
	private BroadcastServerListen listen;
	
	public BroadcastServer(int port) throws IOException {
		super(port);
		init();
	}
	
	public BroadcastServer(String n) throws SocketException {
		setName(n);
		init();
	}
	
	public BroadcastServer(int port, String n) throws SocketException {
		this.port = port;
		setName(n);
		init();
	}
	
	public BroadcastServer() throws SocketException {
		setName("Server");
		init();
	}
	
	private void init() throws SocketException {
		listen = new BroadcastServerListen(this.getName(), port);
		listen.setServerName(getName());
		listen.start();
		start();
	}
	
	@Override
	public void close() throws IOException {
		listen.stopListening();
		super.close();
	}
}
