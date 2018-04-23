package test;
//makes our websocket clients more object oriented


import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;



public class ConnectedClient implements Runnable{

  private WebSocketSession socket;
  private PixionaryServer currentServer;

//  private PrintWriter out;
  private String username;

  private Game gameSession;
  //private final String ACTION_SUCCESS = "success";
  private boolean connected = true;
  
  public int userID;
 public int localScore;
 public int roundScore;
  public boolean guessed;
  
  public ConnectedClient(PixionaryServer currentServer, WebSocketSession socket,String username){
    this.socket = socket;
    this.currentServer = currentServer;
    this.username=username;
  }
  
  public WebSocketSession getSocket()
  {
	  return socket;
  }
  
  public void setSocket(WebSocketSession w)
  {
	  socket = w;
  }

  //shold never be called but is required to exist
  public void run(){
    try{
    	
    //  openComs();
    }
    catch(Exception e){
      delete();
      return;
    }
    while(connected){
    	
    	//  doAction(readInputLine());
    }
  }

 
  public void leaveGame(){
    if(gameSession != null){
       gameSession.removeMemberFromMembersList(this);
       gameSession = null;
    }
  }
	
  //Properly removes client from the server
  public void delete(){
    if(!connected){
      return;
    }
    connected = false;
    //closeComs();
    leaveGame();
    currentServer.removeClient(this);
  }

  //sends string to a specific client (this one)
  public void sendStringToClient(String output){
    if(!connected){
      return;
    }
    try{
    	socket.sendMessage(new TextMessage(output));
      
    }
    catch(Exception e){
      System.out.println("Failed to send '" + output + "' to client.");
      for (Game g: Main.server.gamesList)
		{
			for (ConnectedClient c : Main.server.connectedClients)
			{
				if (c.getSocketSession() != null && c.getGameSession() != null)
				{
				if (c.getGameSession().equals(g) && c.getSocketSession().equals(socket))
				{
					System.out.println("Deleting client from game");
					g.removeMemberFromMembersList(c);
				//	Main.server.connectedClients.remove(c);
				}
				}
			}
		}
    }
  }
//various game logic methods
  public void incrementScore(int a)
  {
	  localScore+=a;
	  roundScore=a;
  }
  
  public int getRoundScore()
  {
	  return roundScore;
  }
  
  public void resetRoundScore()
  {
	   roundScore = 0;
  }
  
  public int getLocalScore()
  {
	  return localScore;
  }
  
  public void setLocalScore(int i)
  {
	  this.localScore = i;
  }
  
  public void setGuessed(boolean b)
  {
	  guessed = b;
  }
  
  public boolean getGuessed()
  {
	  return guessed;
  }
  public String getUsername(){
    return username;
  }
  
  public void setGameSession(Game g)
  {
	  gameSession = g;
  }
  public Game getGameSession()
  {
	  return gameSession;
  }
  
  public WebSocketSession getSocketSession()
  {
	  return socket;
  }

}
