package broadcastChat;

public interface ServerListener {

	public void serverFound(String addr, int port);
	
	public void udpMessageReceived(String msg);
}
