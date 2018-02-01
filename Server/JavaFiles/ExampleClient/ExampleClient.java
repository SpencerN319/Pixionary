import java.net.Socket;
import java.io.IOException;

public class ExampleClient{
  public static void main(String args[]){
    try{
      //We will have our server address instead of "localhost"
      Socket socket = new Socket("localhost", 1337);
      Thread thread = new Thread(new ExampleClientThread(socket));
      thread.start();
    }
    catch(IOException e){
      System.out.println("Failed to connect.");
    }
  }
}
