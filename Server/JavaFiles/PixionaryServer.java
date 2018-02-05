import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.ArrayList;

public class PixionaryServer {

  private ServerSocket serverSocket;
  private final int portNumber;
  private ArrayList<ConnectedClient> connectedClients = new ArrayList<ConnectedClient>();

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
        ConnectedClient newClient = new ConnectedClient(this, socket);
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
