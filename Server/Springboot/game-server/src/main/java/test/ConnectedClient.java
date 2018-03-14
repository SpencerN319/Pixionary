package test;

import java.net.Socket;
import java.io.*;

public class ConnectedClient implements Runnable{

  private Socket socket;
  private PixionaryServer currentServer;
  private BufferedReader in;
  private PrintWriter out;
  private String username = "tempUsername";
  private GamesList gamesList;
  private Game gameSession;
  //private final String ACTION_SUCCESS = "success";
  private boolean connected = true;
  
  public int userID;
  public int localScore;
  public boolean guessed;
  
  public ConnectedClient(PixionaryServer currentServer, GamesList gamesList, Socket socket){
    this.socket = socket;
    this.currentServer = currentServer;
    this.gamesList = gamesList;
  }

  
  public void run(){
    try{
      openComs();
    }
    catch(Exception e){
      delete();
      return;
    }
    while(connected){
    	//wtf is this
    	//  doAction(readInputLine());
    }
  }

  //This method is what connects input strings to their proper actions. It returns a string regarding the success of the action
  /* probably just ignore this whole thing to make it springboot style
  private String doAction(String input){
    if(input == null){
      return null;
    }
    switch(input){
      case "startGame":
        String gameName = readInputLine();
        if(gameName != null){
          boolean gameStarted = startGame(gameName);
          if(gameStarted){
            return ACTION_SUCCESS;
          }
          return "Game already exists";
        }
        delete();
        break;
      case "leaveGame":
        System.out.println("Leaving Game");
        leaveGame();
        return ACTION_SUCCESS;
      default:
        return "Input did not match any expected input.";
    }
    return "User input did not activate in switch.";
  }
	*/
  public String readInputLine(){
    if(!connected){
      return null;
    }
    try{
      String input = in.readLine();
      if(input == null){
        delete();
        return null;
      }
      return input;
    }
    catch(Exception e){
      //Client can be considered disconnected
      delete();
      return null;
    }
  }
/* probably not needed
  private boolean startGame(String gameName){
    if(gameSession != null){
      leaveGame();
    }
    if(!gamesList.gameExists(gameName)){
      //Made new game
      gameSession = gamesList.startGame(this, gameName);
      return true;
    }
    else{
      //Game already exists
      return false;
    }
  }

  public boolean joinGame(String gameName){
    if(gameSession != null){
      leaveGame();
    }
    Game joinGame = gamesList.getGame(gameName);
    if(joinGame != null){
      //Joined game
      joinGame.addMember(this);
      this.gameSession = joinGame;
      return true;
    }
    //Game didn't exist
    return false;
  }
	*/
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
    closeComs();
    leaveGame();
    currentServer.removeClient(this);
  }

  private void openComs() throws IOException{
    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    out = new PrintWriter(socket.getOutputStream(), true);
  }

  private void closeComs(){
    boolean comsOpen = false;
    try{
      if(in != null){
        in.close();
      }
      if(out != null){
        out.close();
      }
      if(socket != null && !socket.isClosed()){
        socket.close();
      }
    }
    catch(Exception e){
      System.out.println("Failed to close comms.");
    }
  }
  //what do dis do
  public void sendStringToClient(String output){
    if(!connected){
      return;
    }
    try{
      out.println(output);
    }
    catch(Exception e){
      System.out.println("Failed to send '" + output + "' to client.");
    }
  }

  public void incrementScore(int a)
  {
	  localScore+=a;
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

}
