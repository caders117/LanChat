package broadcastChat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class BroadcastServerListen extends Thread {

	private DatagramSocket dataSocket;
	private byte[] buffer = new byte[256];
	private boolean running = true;

	public BroadcastServerListen(int port) throws SocketException {
		dataSocket = new DatagramSocket(port);
	}

	public void stopListening() {
		dataSocket.close();
	}
	
	@Override
	public void run() {
		while(true) {
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			try {
				dataSocket.receive(packet);
			} catch (IOException e) {
				if(dataSocket.isClosed())
					break;
			}
			String received = new String(packet.getData(), 0, packet.getLength());
			System.out.println(received);
			if(received.contains("This is client")) {
				buffer = "I received it!".getBytes();
				InetAddress addr = packet.getAddress();
				int port = packet.getPort();
				packet = new DatagramPacket(buffer, buffer.length, addr, port);
				try {
					dataSocket.send(packet);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		System.out.println("Stopped listening for client udp");
	}
}
