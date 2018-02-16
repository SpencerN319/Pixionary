package Client;

/**
 * Created by Steven on 2/2/2018.
 */

import java.net.Socket;
import java.io.IOException;

public class Client{
    public static void main(String args[]){
        try{
            //We will have our server address instead of "localhost"
            Socket socket = new Socket("localhost", 1337);
            Thread thread = new Thread(new MainThread(socket));
            thread.start();
        }
        catch(IOException e){
            System.out.println("Failed to connect.");
        }
    }
}