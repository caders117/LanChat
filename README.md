# LanChat
Server and Client java classes.
In simpleChat:
 - Server starts and listens for a client wanting to establish TCP connection.
 - Server IP and port number have to be specified/known before connection.
 - Contains listener for receiving message.
 - Test classes take console I/O and send it between client/server.

In broadcastChat:
 - Server starts on a default port of 8080.  If that port is not available, it keeps incrementing
   the port until it finds an available one. 
 - Then it listens for a client's UDP packet.
 - Client loops through 100 ports on the subnet mask 255.255.255.255, starting at a default
   port of 8080, and broadcasts a UDP packet to each port on the local network.
 - Once the server receives a UDP packet, it replies with another UDP packet.
 - The client receives the server's UDP packet and sends a request for a TCP connection.
 - Contains a listener for receiving messages (simpleChat) and a listener for discovering
   the server with UDP broadcasting.

Yay multithreading!
Yay networking!
Hope to make a nice looking chat GUI with an option to start a server or join other people's servers.
