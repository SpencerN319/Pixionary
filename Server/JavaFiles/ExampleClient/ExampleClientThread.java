import java.net.Socket;
import java.io.*;
import java.util.Scanner;

public class ExampleClientThread implements Runnable{

  private Socket socket;

  public ExampleClientThread(Socket socket){
    this.socket = socket;
  }
  //TODO Levi Clark - Send data to server from here
  public void run(){
    try{
      System.out.println("Enter any data and press enter:");
      String input;
      try{
        Scanner scanner = new Scanner(System.in);
        input = scanner.nextLine();
      }
      catch(Exception e){
        System.out.println("Failed to get input.");
      }
      System.out.println("Thank you.");
      socket.close();
      System.out.println("Socket closed.");
    }
    catch(Exception e){
      System.out.println("Failed to close socket.");
    }
  }

}
