//create or start a game, we shall see. right now its just hello world
package test;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {

	@RequestMapping("/creategame")
	//creates game, begins socket connection
	public String makeGame(String gameName, String category)
	{	
		Game g;
		try {
			
		ServerSocket serverSocket = Main.server.serverSocket;
        Socket socket = serverSocket.accept();
        ConnectedClient newClient = new ConnectedClient(Main.server,  socket);
        Main.server.connectedClients.add(newClient);
        System.out.println("Now serving " + Main.server.connectedClients.size() + " clients.");
        Thread newThread = new Thread(newClient);
        newThread.start();
       
		g = new Game (newClient, gameName, category);
		Main.server.gamesList.add(g);
	
		}catch(IOException e){
	        System.out.println("Accept failed on port " + Main.server.portNumber);
	        return ("Accept failed on port " + Main.server.portNumber);
	      }
		
		  try {
		       
	          Connection conn1;
	          String dbUrl = "jdbc:mysql://mysql.cs.iastate.edu:3306/db309sb3";
	          String user = "dbu309sb3";
	          String password = "Fx3tvTaq";
	               conn1 = DriverManager.getConnection(dbUrl, user, password);
	          System.out.println("*** Connected to the database ***");
	          
	          Statement statement = conn1.createStatement();
	          
	          //this isn't done yet
	          statement.executeUpdate("INSERT INTO Games (Host, Category, Name, ID, Active) VALUES "
	          		+ "('" + g.getHostName() +"', '" + g.getCategory() + "', '" +g.getName() + "', '" + g.getID() + "', '" + g.getGameStatus() + "');");
	        
	       
	          
	      } catch (SQLException e) {
	          System.out.println("SQLException: " + e.getMessage());
	          System.out.println("SQLState: " + e.getSQLState());
	          System.out.println("VendorError: " + e.getErrorCode());
	      }
		
		
			return "Game has been created";
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
				 try {
				        
			          Connection conn1;
			          String dbUrl = "jdbc:mysql://mysql.cs.iastate.edu:3306/db309sb3";
			          String user = "dbu309sb3";
			          String password = "Fx3tvTaq";
			               conn1 = DriverManager.getConnection(dbUrl, user, password);
			          System.out.println("*** Connected to the database ***");
			          
			          Statement statement = conn1.createStatement();


			          statement.executeUpdate("UPDATE Games SET Active =TRUE WHERE ID ='"+g.getHostName()+"';");
			          
			      } catch (SQLException e) {
			          System.out.println("SQLException: " + e.getMessage());
			          System.out.println("SQLState: " + e.getSQLState());
			          System.out.println("VendorError: " + e.getErrorCode());
			      }
				break;
			}
	     
		}
		
		return("game has been started");
	}
	

}
