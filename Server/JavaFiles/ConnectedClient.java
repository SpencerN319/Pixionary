import java.net.Socket;
import java.io.*;

public class ConnectedClient implements Runnable{

  private Socket socket;
  private PixionaryServer currentServer;
  private BufferedReader in;
  private PrintWriter out;

  public ConnectedClient(PixionaryServer currentServer, Socket socket){
    this.socket = socket;
    this.currentServer = currentServer;
  }

  public void run(){
    System.out.println("New client has joined");
    try{
      openComs();
    }
    catch(Exception e){
      System.out.println("Error connecting to client, disconnecting client");
      disconnectClient();
      return;
    }
    while(true){
      try{
        String input = in.readLine();
        if(input == null){
          disconnectClient();
          return;
        }
        System.out.println(input);
      }
      catch(Exception e){
        //Client can be considered disconnected
        System.out.println("Hit error during getting input, disconncting client");
        disconnectClient();
        return;
      }
    }
  }

  //Properly removes client from the server
  public void disconnectClient(){
    closeComs();
    currentServer.removeClient(this);
    System.out.println("Client removed");
  }

  private void openComs() throws IOException{
    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    out = new PrintWriter(socket.getOutputStream());
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

}
