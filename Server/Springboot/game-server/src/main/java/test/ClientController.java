//go here to connect to a game (and just connect in general)
package test;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
//import java.net.Socket;
//import java.io.IOException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class ClientController {

	@RequestMapping("/connect")
	public void connect(Game g)
	{
		 try{
			 ServerSocket serverSocket = Main.server.serverSocket;
		        Socket socket = serverSocket.accept();
		        ConnectedClient newClient = new ConnectedClient(Main.server, Main.server.gamesList, socket);
		        Main.server.connectedClients.add(newClient);
		        System.out.println("Now serving " + Main.server.connectedClients.size() + " clients.");
		        Thread newThread = new Thread(newClient);
		        newThread.start();
		        g.addMember(newClient);
		      }
		      catch(IOException e){
		        System.out.println("Accept failed on port " + Main.server.portNumber);
		      }
		
	}
}
