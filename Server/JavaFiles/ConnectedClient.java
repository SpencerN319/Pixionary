import java.net.Socket;
import java.io.*;

public class ConnectedClient implements Runnable{

  private Socket socket;
  private PixionaryServer currentServer;
  private int increment = 0;

  public ConnectedClient(PixionaryServer currentServer, Socket socket){
    this.socket = socket;
    this.currentServer = currentServer;
  }

  public void run(){
    BufferedReader in;
    PrintWriter out;
    System.out.println("Began new client thread!");
    while(true){
      System.out.println(increment++);
      try{
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream());
        if(in.read() == -1){
          //Client socket is disconnected
          disconnectClient();
          return;
        }
        String newInput = in.readLine();
        System.out.println("Hit");
      }
      catch(Exception e){
        //Client can be considered disconnected
        disconnectClient();
        return;
      }
    }
  }

  //Properly removes client from the server
  public void disconnectClient(){
    currentServer.removeClient(this);
    System.out.println("Client removed!");
  }

}
