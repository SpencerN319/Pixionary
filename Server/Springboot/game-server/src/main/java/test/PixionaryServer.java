package test;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.ArrayList;


//Architecture rules of the server:
//To remove anything from the server (playlist, game, user, etc.) you must call it's delete() function.
//Subsequently, it must be possible to completely remove an entity from the server by calling it's delete function.
//(There should be no need to remove it's references manually)


public class PixionaryServer {

  public ServerSocket serverSocket;
  public final int portNumber;
  public ArrayList<ConnectedClient> connectedClients = new ArrayList<ConnectedClient>();

  //public GamesList gamesList = new GamesList();
  public ArrayList<Game> gamesList = new ArrayList<Game>();
  public PixionaryServer(int portNumber){
    this.portNumber = portNumber;
    try{
      serverSocket = new ServerSocket(portNumber);
      System.out.println("Server has initialized socket without error.");
    }
    catch(IOException e){
      System.err.println("Couldn't listen on port " + portNumber);
      System.exit(1);
    }
  }
//deprecated but still needs to exist as a method I think
  public void start(){
    //Accept clients, and start their threads
	  //maybe do this here also a possibility
    while(true){
    //  try{
      //  Socket socket = serverSocket.accept();
       // ConnectedClient newClient = new ConnectedClient(this, socket);
        //connectedClients.add(newClient);
        //System.out.println("Now serving " + connectedClients.size() + " clients.");
        System.out.println("WHY IS pixionaryserver.start() being called?");
    	  //Thread newThread = new Thread(newClient);
        //newThread.start();
    //  }
     // catch(IOException e){
      //  System.out.println("Accept failed on port " + portNumber);
      //}
    }
  }

  public void removeClient(ConnectedClient client){
    connectedClients.remove(client);
    System.out.println("Now serving " + connectedClients.size() + " clients.");
  }
  

}
