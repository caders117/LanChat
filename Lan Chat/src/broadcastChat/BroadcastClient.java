package broadcastChat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import simpleChat.Client;

public class BroadcastClient extends Client {
	
	private DatagramSocket dataSoc;
	private InetAddress addr;
	private int port = 8080;
	private BroadcastClientListen listen;
	
	private List<ServerListener> serversAvailable = new ArrayList<ServerListener>();

	public BroadcastClient() throws SocketException, UnknownHostException {
		dataSoc = new DatagramSocket();
		addr = InetAddress.getByName("255.255.255.255");
		listen = new BroadcastClientListen(this, dataSoc);
		listen.start();
	}
	
	public BroadcastClient(int port) throws SocketException, UnknownHostException {
		this.port = port;
		dataSoc = new DatagramSocket();
		addr = InetAddress.getByName("255.255.255.255");
		listen = new BroadcastClientListen(this, dataSoc);
		listen.start();
	}
	
	public void broadcast(String message) throws IOException {
		dataSoc.setBroadcast(true);
		
		byte[] buffer = message.getBytes();
		int end = port + 100;
		while(port < end) {
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, addr, port);
			dataSoc.send(packet);
			port++;
		}
	}
	
	@Override
	public void close() throws IOException {
		listen.stopListening();
		super.close();
	}
	
	public void connect(String addr, int port) throws UnknownHostException, IOException {
		setAddress(addr);
		setPort(port);
		startListening();
	}
	
	public List<ServerListener> getServerListeners() {
		return serversAvailable;
	}

	public void addServerListener(ServerListener toAdd) {
		serversAvailable.add(toAdd);
	}

	public void removeServerListener(ServerListener toRemove) {
		serversAvailable.remove(toRemove);
	}

	public void foundServer(String addr, int port) {
		// Notify everybody that may be interested.
		for (ServerListener sl : serversAvailable)
			sl.serverFound(addr, port);
	}
}
