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

  private ServerSocket serverSocket;
  private final int portNumber;
  private ArrayList<ConnectedClient> connectedClients = new ArrayList<ConnectedClient>();

  private GamesList gamesList = new GamesList();

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

  public void start(){
    //Accept clients, and start their threads
    while(true){
      try{
        Socket socket = serverSocket.accept();
        ConnectedClient newClient = new ConnectedClient(this, gamesList, socket);
        connectedClients.add(newClient);
        System.out.println("Now serving " + connectedClients.size() + " clients.");
        Thread newThread = new Thread(newClient);
        newThread.start();
      }
      catch(IOException e){
        System.out.println("Accept failed on port " + portNumber);
      }
    }
  }

  public void removeClient(ConnectedClient client){
    connectedClients.remove(client);
    System.out.println("Now serving " + connectedClients.size() + " clients.");
  }
  

}
