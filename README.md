# LanChat
Server and Client java classes.

In simpleChat package:
 - Server starts and listens for a client wanting to establish TCP connection.
 - Server IP and port number have to be specified/known before connection.
 - Contains listener for receiving message.
 - Test classes take console I/O and send it between client/server.

In broadcastChat package:
 - Server starts on a default port of 8080.  If that port is not available, it keeps incrementing
   the port until it finds an available one. 
 - Then it listens for a client's UDP packet.
 - Client loops through 100 ports on the subnet mask 255.255.255.255, starting at a default
   port of 8080, and broadcasts a UDP packet to each port on the local network.
 - Once the server receives a UDP packet, it replies with another UDP packet.
 - The client receives the server's UDP packet and sends a request for a TCP connection.
 - Contains a listener for receiving messages (simpleChat) and a listener for discovering
   the server with UDP broadcasting.
   
In gui package:
 - MenuScreen.java - "Main Menu" for joining servers.
 - ClientGUI.java - JPanel for a client interacting with the chosen server.
 - GUIServer.java - Separate server class for handling information that passes through it in the GUI application.
 
In jars folder:
 - Names go together.
 - Client + Server pairs Servers have to be started first.
 - Client + Server pairs Server talks to multiple clients, looks clean on Server screen, icky on Client screen.
 - User_interface_finished.jar is the full application.  It doesn't work :(
 
Networking and Multithreading were both new.
