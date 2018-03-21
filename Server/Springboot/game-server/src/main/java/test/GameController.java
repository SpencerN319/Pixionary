//create or start a game, we shall see. right now its just hello world
package test;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {

	@RequestMapping("/creategame")
	//creates game, begins socket connection
	public String makeGame(String gameName, String category)
	{	
		try {
			
		ServerSocket serverSocket = Main.server.serverSocket;
        Socket socket = serverSocket.accept();
        ConnectedClient newClient = new ConnectedClient(Main.server,  socket);
        Main.server.connectedClients.add(newClient);
        System.out.println("Now serving " + Main.server.connectedClients.size() + " clients.");
        Thread newThread = new Thread(newClient);
        newThread.start();
       
		Game g = new Game (newClient, gameName, category);
		Main.server.gamesList.add(g);
	
		}catch(IOException e){
	        System.out.println("Accept failed on port " + Main.server.portNumber);
	        return ("Accept failed on port " + Main.server.portNumber);
	      }
		
			return "Game has been started";
	}
	
	
	@RequestMapping("/joingame")

	public String joinGame(int gameID)
	{
		try {
			
			ServerSocket serverSocket = Main.server.serverSocket;
	        Socket socket = serverSocket.accept();
	        ConnectedClient newClient = new ConnectedClient(Main.server, socket);
	        Main.server.connectedClients.add(newClient);
	        System.out.println("Now serving " + Main.server.connectedClients.size() + " clients.");
	        Thread newThread = new Thread(newClient);
	        newThread.start();
	        
			for (Game g : Main.server.gamesList)
			{
				if (g.getID() == gameID)
				{
					g.addMember(newClient);
					break;
				}
		     
			}
			
			}catch(IOException e){
		        System.out.println("Accept failed on port " + Main.server.portNumber);
		        return ("Accept failed on port " + Main.server.portNumber);

		      }
			
			
		

			return "game has been joined";
			/* if (numplayers = maximum
			 g.startGame();
			 */
	}
	@RequestMapping("/startgame")
	public String startGame(int gameID)
	{
		for (Game g : Main.server.gamesList)
		{
			if (g.getID() == gameID)
			{
				g.startGame();
				break;
			}
	     
		}
		return("game has been started");
	}
	

}
