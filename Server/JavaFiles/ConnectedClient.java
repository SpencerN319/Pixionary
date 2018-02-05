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

  public ConnectedClient(PixionaryServer currentServer, GamesList gamesList, Socket socket){
    this.socket = socket;
    this.currentServer = currentServer;
    this.gamesList = gamesList;
  }

  public void run(){
    try{
      openComs();
      System.out.println("Player disconnected");
    }
    catch(Exception e){
      delete();
      return;
    }
    while(true){
      try{
        String input = in.readLine();
        if(input == null){
          delete();
          return;
        }
        System.out.println("Input recieved: " + input);
      }
      catch(Exception e){
        //Client can be considered disconnected
        delete();
        return;
      }
    }
  }

  public void startGame(String gameName){
    if(gameSession != null){
      leaveGame();
    }
    if(!gamesList.gameExists(gameName)){
      gameSession = gamesList.startGame(this, gameName);
    }
    else{

    }
  }

  public void joinGame(String gameName){
    if(gameSession != null){
      leaveGame();
    }
    Game joinGame = gamesList.getGame(gameName);
    if(joinGame != null){
      joinGame.addMember(this);
      this.gameSession = joinGame;
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
    closeComs();
    if(gameSession != null){
      leaveGame();
    }
    currentServer.removeClient(this);
    System.out.println("Client removed");
  }

  private void openComs() throws IOException{
    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    out = new PrintWriter(socket.getOutputStream(), true);
  }

  private void closeComs(){
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

  public void sendStringToClient(String output){
    try{
      out.println(output);
    }
    catch(Exception e){
      System.out.println("Failed to send '" + output + "' to client.");
    }
  }

  public String getUsername(){
    return username;
  }

}
