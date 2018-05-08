package broadcastChat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.UnknownHostException;

import simpleChat.Client;

public class BroadcastClientListen extends Thread {
	
	private DatagramSocket dataSoc;
	private byte[] buffer = new byte[256];
	BroadcastClient client;

	public BroadcastClientListen(BroadcastClient cli, DatagramSocket ds) {
		dataSoc = ds;
		client = cli;
	}
	
	public void stopListening() {
		dataSoc.close();
	}
	
	@Override
	public void run() {
		while(!client.isClosed()) {
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			try {
				dataSoc.receive(packet); 
			} catch (IOException e) {
				if(dataSoc.isClosed())
					break;
			}
			String received = new String(packet.getData(), 0, packet.getLength());
		//	System.out.println(received);
			if(received.contains("I received it!")) {
				client.foundServer(packet.getAddress().getHostAddress(), packet.getPort());
				client.receiveUDPMessage(received.substring(14));
			}
		}
		System.out.println("Stopped listening for server udp reply");
	}
}
