package test;
//calls appropriate methods for client requests
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;




@Component
public class SocketHandler extends TextWebSocketHandler {
	
	CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<WebSocketSession>();

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message)
			throws InterruptedException, IOException 
	{

        
		System.out.println("Socket attempting to connect");
		for(WebSocketSession webSocketSession : sessions) {
		System.out.println("Message incoming");
		String value =	message.getPayload();
		String[] parts = value.split(",");
		//if the user wants to create a game
		if (parts[0].equals("create"))
		{
			Game g;
			ConnectedClient newClient = new ConnectedClient(Main.server,  session,parts[1]);
	        Main.server.connectedClients.add(newClient);
	        System.out.println("Now serving " + Main.server.connectedClients.size() + " clients.");
			System.out.println("creating game"+ parts[1] +" with category "+ parts[2] + " of size "+parts[3]);
			webSocketSession.sendMessage(new TextMessage("Created"));
			g = new Game (newClient, parts[1], parts[2], Integer.parseInt(parts[3]));
			Main.server.gamesList.add(g);
			
			 try {
			       //add new game to active games
		          Connection conn1;
		          String dbUrl = "jdbc:mysql://mysql.cs.iastate.edu:3306/db309sb3";
		          String user = "dbu309sb3";
		          String password = "Fx3tvTaq";
		               conn1 = DriverManager.getConnection(dbUrl, user, password);
		          System.out.println("*** Connected to the database ***");
		          
		          Statement statement = conn1.createStatement();
		          
		          //this probably isn't right
		          statement.executeUpdate("INSERT INTO Active (host_name, category, players) VALUES "
		          		+ "('" + g.getHostName() +"', '" + g.getCategory() + "', '"+ g.gameSize + "');");
		        
		       
		          
		      } catch (SQLException e) {
		          System.out.println("SQLException: " + e.getMessage());
		          System.out.println("SQLState: " + e.getSQLState());
		          System.out.println("VendorError: " + e.getErrorCode());
		      }
			
		}else if (parts[0].equals("join"))
		{
			//TODO: update db when player joins
			//if the user wants to join a game
			 ConnectedClient newClient = new ConnectedClient(Main.server, session,parts[2]);
		        Main.server.connectedClients.add(newClient);
		        System.out.println("Now serving " + Main.server.connectedClients.size() + " clients.");
			System.out.println("joining game");
			
			for (Game g : Main.server.gamesList)
			{
				if (g.getHostName().equals(parts[1]))
				{
					if (g.numPlayers == g.gameSize)
					{
						
						newClient.sendStringToClient("FULL");
						System.out.println("Client failed to join full game. there are "+ g.numPlayers +" in this game for " + g.gameSize);
						
					}else
					{
					g.sendStringToAllMembers("newmember " + parts[2]);
					g.addMember(newClient);
					newClient.sendStringToClient("Currentplayers");
					//this currently includes the player
					for (ConnectedClient c : g.gameMembers)
					{
						newClient.sendStringToClient("Player: " + c.getUsername());
					}
					newClient.sendStringToClient("Endplayers");
					break;
					}
				}
		     
			}
			
		}else if (parts[0].equals("start"))
		{
		    //if the user wants to start their game
			for (Game g : Main.server.gamesList)
			{
				if (g.getHostName().equals(parts[1]))
				{
					Thread t = new Thread(g);
					//g.startGame();
					t.start();
					 try {
					        //removes game from joinable games list
				          Connection conn1;
				          String dbUrl = "jdbc:mysql://mysql.cs.iastate.edu:3306/db309sb3";
				          String user = "dbu309sb3";
				          String password = "Fx3tvTaq";
				               conn1 = DriverManager.getConnection(dbUrl, user, password);
				          System.out.println("*** Connected to the database ***");
				          
				          Statement statement = conn1.createStatement();


				          statement.executeUpdate("DELETE FROM Active WHERE host_name ='"+g.getHostName()+"';");
				          
				      } catch (SQLException e) {
				          System.out.println("SQLException: " + e.getMessage());
				          System.out.println("SQLState: " + e.getSQLState());
				          System.out.println("VendorError: " + e.getErrorCode());
				      }
					break;
				}
				
				
			System.out.println("Starting game hosted by"+parts[1]);
			
			}
		}else if (parts[0].equals("guess"))
		{
			System.out.println("attempting to guess");
		    //if the user is in a game and wishes to make a guess at the word
			for (Game g: Main.server.gamesList)
			{
				if (g.getHostName().equals(parts[2]))
				{
				for (ConnectedClient c : g.gameMembers)
				{
					if (c.getUsername().equals(parts[3]))
					{
						System.out.println("Sending guess to game");
						g.getGuess(c, parts[1]);
					}
				}
				}
			}
			System.out.println("making guess "+ parts[1]);
		}else if (parts[0].equals("reconnect"))
		{
			for (Game g: Main.server.gamesList)
			{
				
				if (g.findOrphan(parts[1]))
				{
					ConnectedClient newClient = new ConnectedClient(Main.server, session,parts[1]);
					g.addRescuedOrphan(newClient);

					break;
				}
			}
			
		}else if (parts[0].equals("playagain"))
		{
		    //if the user is in a game and wishes to make a guess at the word
			for (Game g: Main.server.gamesList)
			{
				for (ConnectedClient c : Main.server.connectedClients)
				{
					if (c.getGameSession().equals(g) && c.getSocketSession().equals(session))
					{
						g.addPlayAgain(c);					
					}
				}
			}
		}else
		{
			System.out.println("Invalid message");
			webSocketSession.sendMessage(new TextMessage("Message not recognized (or blank for testing)"));
		}
		}
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		//the messages will be broadcasted to all users.
		sessions.add(session);
	}
	
}
