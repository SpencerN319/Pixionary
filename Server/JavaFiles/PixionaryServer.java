import java.net.ServerSocket;

public class PixionaryServer {

  public static void main(String[] args){
    int portNumber 1337;
    serverSocket = null;
    try{
      ServerSocket serverSocket = new ServerSocket(portNumber);
    }
    catch(IOException e){
      System.err.println("Couldn't listen on port " + portNumber);
      System.exit(1);
    }
  }

}
