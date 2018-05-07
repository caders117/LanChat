package broadcastChat;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Test {
	
	public static void main(String[] args) {
		InetAddress addr = null;
	    try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    String hostname = addr.getHostName();
	    System.out.println(hostname);
	}
}
