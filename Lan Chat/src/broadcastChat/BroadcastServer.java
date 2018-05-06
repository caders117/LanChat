package broadcastChat;

import java.io.IOException;
import java.net.SocketException;

import simpleChat.Server;

public class BroadcastServer extends Server {
	
	private BroadcastServerListen listen;
	
	public BroadcastServer(int port) throws IOException {
		super(port);
		listen = new BroadcastServerListen(port);
		listen.start();
		start();
	}
	
	public BroadcastServer() throws SocketException {
		listen = new BroadcastServerListen(port);
		listen.start();
		start();
	}
	
	@Override
	public void close() throws IOException {
		listen.stopListening();
		super.close();
	}
}
