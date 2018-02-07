import java.net.Socket;
import java.io.*;
import java.util.Scanner;

public class ExampleClientThread implements Runnable{

  private Socket socket;

  public ExampleClientThread(Socket socket){
    this.socket = socket;
  }

  public void run(){
    System.out.println("Enter any data and press enter:");
    while(true){
      try{
          String input;
          Scanner scanner = new Scanner(System.in);
          input = scanner.nextLine();
          PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
          System.out.println("Sending " + input);
          out.println(input);
        }
        catch(Exception e){
          System.out.println("Failed to get and send input.");
        }
    }
  }
  
}
